package codezap.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.dto.response.FindCategoryResponse;
import codezap.category.repository.CategoryRepository;
import codezap.global.DatabaseIsolation;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.MemberRepository;

@SpringBootTest
@DatabaseIsolation
class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryService categoryService;

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class CreateCategoryTest {

        @Test
        @DisplayName("카테고리 생성 성공")
        @Transactional
        void createCategorySuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            String categoryName = "categoryName";
            CreateCategoryRequest request = new CreateCategoryRequest(categoryName);

            CreateCategoryResponse response = categoryService.create(member, request);
            Category savedCategory = categoryRepository.fetchById(response.id());

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(1L),
                    () -> assertThat(savedCategory.getName()).isEqualTo(categoryName),
                    () -> assertThat(savedCategory.getMember()).isEqualTo(member)
            );
        }

        @Test
        @DisplayName("카테고리 생성 성공: 다른 멤버, 중복된 이름의 카테고리 이름 생성")
        @Transactional
        void createCategorySuccessWithOtherMemberAndSameName() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
            String duplicatedCategoryName = "category";
            categoryRepository.save(new Category(duplicatedCategoryName, member));

            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(duplicatedCategoryName);
            CreateCategoryResponse createCategoryResponse = categoryService.create(otherMember, createCategoryRequest);
            Category savedCategory = categoryRepository.fetchById(createCategoryResponse.id());

            assertAll(
                    () -> assertThat(createCategoryResponse.id()).isEqualTo(2L),
                    () -> assertThat(savedCategory.getName()).isEqualTo(duplicatedCategoryName),
                    () -> assertThat(savedCategory.getMember()).isEqualTo(otherMember)
            );
        }

        @Test
        @DisplayName("카테고리 생성 실패: 동일한 멤버, 중복된 이름의 카테고리 이름 생성")
        void createCategoryFailWithSameMemberAndDuplicateName() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            String duplicatedCategoryName = "category";
            categoryRepository.save(new Category(duplicatedCategoryName, member));

            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(duplicatedCategoryName);

            assertThatThrownBy(() -> categoryService.create(member, createCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + duplicatedCategoryName + "인 카테고리가 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("멤버로 카테고리 조회 테스트")
    class FindAllCategoryByMemberTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
            Category category3 = categoryRepository.save(new Category("notMyCategory", otherMember));

            FindAllCategoriesResponse categoryByMember = categoryService.findAllByMember(member);

            assertThat(categoryByMember.categories()).hasSize(2)
                    .containsExactly(FindCategoryResponse.from(category1), FindCategoryResponse.from(category2))
                    .doesNotContain(FindCategoryResponse.from(category3));
        }

        @Test
        @DisplayName("성공 : 존재하지 않는 멤버로 조회를 하면 DB 에러가 발생한다.")
        void failWithNotExistMember() {
            Member notExistMember = MemberFixture.createFixture("notExist");

            assertThatThrownBy(() -> categoryService.findAllByMember(notExistMember))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class);
        }
    }

    @Nested
    @DisplayName("카테고리 전체 조회 테스트")
    class FindAllCategoryTest {

        @Test
        @DisplayName("카테고리 전체 조회 테스트")
        void findAllCategoriesSuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            categoryRepository.save(new Category("category1", member));
            categoryRepository.save(new Category("category2", member));

            FindAllCategoriesResponse findAllCategoriesResponse = categoryService.findAll();

            assertThat(findAllCategoriesResponse.categories()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("카테고리 단건 조회 테스트")
    class FetchByIdTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category savedCategory = categoryRepository.save(new Category("categoryName", member));

            Category actual = categoryService.fetchById(savedCategory.getId());

            assertThat(actual).isEqualTo(savedCategory);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 id 값으로 카테고리 조회")
        void failWithNotSavedId() {
            long notSavedCategoryId = 100L;

            assertThatThrownBy(() -> categoryService.fetchById(notSavedCategoryId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedCategoryId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("카테고리 수정 테스트")
    class UpdateCategoryTest {

        @Test
        @DisplayName("카테고리 수정 성공")
        void updateCategorySuccess() {
            String updateCategoryName = "updateName";
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category savedCategory = categoryRepository.save(new Category("category1", member));

            categoryService.update(member, savedCategory.getId(),
                    new UpdateCategoryRequest(updateCategoryName));

            assertThat(categoryRepository.fetchById(savedCategory.getId()).getName()).isEqualTo(updateCategoryName);
        }

        @Test
        @DisplayName("카테고리 수정 실패: 권한 없음")
        void updateCategoryFailWithUnauthorized() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category savedCategory = categoryRepository.save(new Category("category1", member));
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));

            UpdateCategoryRequest request = new UpdateCategoryRequest("updateName");

            assertThatThrownBy(() -> categoryService.update(otherMember, savedCategory.getId(), request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리에 대한 권한이 없습니다.");
        }

        @Test
        @DisplayName("카테고리 수정 실패: 이미 존재하는 카테고리 이름")
        void duplicatedCategoryName() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));

            UpdateCategoryRequest request = new UpdateCategoryRequest(category1.getName());

            assertThatThrownBy(() -> categoryService.update(member, category2.getId(), request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + category1.getName() + "인 카테고리가 이미 존재합니다.");
        }

        @Test
        @DisplayName("카테고리 수정 실패: 현재와 동일한 카테고리 이름")
        void notChangedCategoryName() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category category = categoryRepository.save(new Category("category", member));

            UpdateCategoryRequest request = new UpdateCategoryRequest(category.getName());

            assertThatThrownBy(() -> categoryService.update(member, category.getId(), request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + category.getName() + "인 카테고리가 이미 존재합니다.");
        }

        @Test
        @DisplayName("카테고리 수정 실패: 존재하지 않는 카테고리 id")
        void notSavedCategoryId() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            UpdateCategoryRequest request = new UpdateCategoryRequest("categoryName");
            long notSavedId = 100L;

            assertThatThrownBy(() -> categoryService.update(member, notSavedId, request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }
    }
}
