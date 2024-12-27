package codezap.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateAllCategoriesRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.CreateCategoryResponse;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.dto.response.FindCategoryResponse;
import codezap.fixture.MemberFixture;
import codezap.global.ServiceTest;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.template.domain.Template;

class CategoryServiceTest extends ServiceTest {

    @Autowired
    private CategoryService sut;

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class CreateCategoryTest {

        @Test
        @DisplayName("카테고리 생성 성공")
        void createCategorySuccess() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            String categoryName = "categoryName";
            CreateCategoryRequest request = new CreateCategoryRequest(categoryName, 0);

            CreateCategoryResponse response = sut.create(member, request);

            Category savedCategory = categoryRepository.fetchById(response.id());
            assertAll(
                    () -> assertThat(response.id()).isEqualTo(1L),
                    () -> assertThat(savedCategory.getName()).isEqualTo(categoryName),
                    () -> assertThat(savedCategory.getMember()).isEqualTo(member)
            );
        }

        @Test
        @DisplayName("카테고리 생성 성공: 다른 멤버, 중복된 이름의 카테고리 이름 생성")
        void createCategorySuccessWithOtherMemberAndSameName() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
            String duplicatedCategoryName = "category";
            categoryRepository.save(new Category(duplicatedCategoryName, member, 0));

            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(duplicatedCategoryName, 0);
            CreateCategoryResponse createCategoryResponse = sut.create(otherMember, createCategoryRequest);
            Category savedCategory = categoryRepository.fetchById(createCategoryResponse.id());

            assertAll(
                    () -> assertThat(createCategoryResponse.id()).isEqualTo(2L),
                    () -> assertThat(savedCategory.getName()).isEqualTo(duplicatedCategoryName),
                    () -> assertThat(savedCategory.getMember()).isEqualTo(otherMember)
            );
        }

        @Test
        @DisplayName("카테고리 생성 성공: 멤버별 마지막 순서로 생성")
        void createCategorySuccessWithLastOrdinalByMember() {
            Member member1 = memberRepository.save(MemberFixture.getFirstMember());
            Member member2 = memberRepository.save(MemberFixture.getSecondMember());
            categoryRepository.save(new Category("category1", member1, 0));
            categoryRepository.save(new Category("category2", member1, 1));
            categoryRepository.save(new Category("category3", member2, 0));

            String categoryName = "category4";
            CreateCategoryRequest request = new CreateCategoryRequest(categoryName, 2);

            CreateCategoryResponse response = sut.create(member1, request);

            Category savedCategory = categoryRepository.fetchById(response.id());
            assertThat(savedCategory.getOrdinal()).isEqualTo(2L);
        }

        @Test
        @DisplayName("카테고리 생성 실패: 동일한 멤버, 중복된 이름의 카테고리 이름 생성")
        void createCategoryFailWithSameMemberAndDuplicateName() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            String duplicatedCategoryName = "category";
            categoryRepository.save(new Category(duplicatedCategoryName, member, 1));

            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(duplicatedCategoryName, 2);

            assertThatThrownBy(() -> sut.create(member, createCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + duplicatedCategoryName + "인 카테고리가 이미 존재합니다.");
        }
    }

    @Nested
    @DisplayName("멤버 ID로 카테고리 조회 테스트")
    class FindAllCategoryByMemberTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category1 = categoryRepository.save(new Category("category1", member, 1));
            Category category2 = categoryRepository.save(new Category("category2", member, 2));
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
            Category category3 = categoryRepository.save(new Category("notMyCategory", otherMember, 1));

            FindAllCategoriesResponse categoryByMember = sut.findAllByMemberId(member.getId());

            assertThat(categoryByMember.categories()).hasSize(2)
                    .containsExactly(FindCategoryResponse.from(category1), FindCategoryResponse.from(category2))
                    .doesNotContain(FindCategoryResponse.from(category3));
        }

        @Test
        @DisplayName("성공 : 존재하지 않는 멤버로 조회를 하면 빈 리스트를 반환한다.")
        void failWithNotExistMember() {
            long nonExistentMemberId = 100L;

            var actual = sut.findAllByMemberId(nonExistentMemberId).categories();

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("카테고리 전체 조회 테스트")
    class FindAllCategoryTest {

        @Test
        @DisplayName("성공")
        void findAllCategoriesSuccess() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());

            categoryRepository.save(new Category("category1", member, 1));
            categoryRepository.save(new Category("category2", member, 2));

            FindAllCategoriesResponse findAllCategoriesResponse = sut.findAll();

            assertThat(findAllCategoriesResponse.categories()).hasSize(2);
        }

        @Test
        @DisplayName("성공 : 카테고리가 존재하지 않으면 빈 리스트를 반환한다.")
        void findAllCategoriesEmptyList() {
            FindAllCategoriesResponse findAllCategoriesResponse = sut.findAll();

            assertThat(findAllCategoriesResponse.categories()).isEmpty();
        }
    }

    @Nested
    @DisplayName("카테고리 단건 조회 테스트")
    class FetchByIdTest {

        @Test
        @DisplayName("성공")
        void success() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category savedCategory = categoryRepository.save(new Category("categoryName", member, 1));

            Category actual = sut.fetchById(savedCategory.getId());

            assertThat(actual).isEqualTo(savedCategory);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 id 값으로 카테고리 조회")
        void failWithNotSavedId() {
            long notSavedCategoryId = 100L;

            assertThatThrownBy(() -> sut.fetchById(notSavedCategoryId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedCategoryId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("카테고리 편집 테스트")
    class UpdateCategoryTest {

        Member member;
        Category defaultCategory;

        @BeforeEach
        void saveDefaultCategory() {
            member = memberRepository.save(MemberFixture.getFirstMember());
            defaultCategory = categoryRepository.save(Category.createDefaultCategory(member));
        }

        @Test
        @DisplayName("카테고리 편집 성공")
        void updateCategoriesSuccess() {
            String createCategoryName = "createName";
            String updateCategoryName = "updateName1";
            Category category1 = categoryRepository.save(new Category("category1", member, 1));
            Category category2 = categoryRepository.save(new Category("category2", member, 2));

            CreateCategoryRequest createRequest = new CreateCategoryRequest(createCategoryName, 1);
            UpdateCategoryRequest updateRequest = new UpdateCategoryRequest(category1.getId(), updateCategoryName, 2);

            sut.updateCategories(member, new UpdateAllCategoriesRequest(
                    List.of(createRequest),
                    List.of(updateRequest),
                    List.of(category2.getId())));

            assertAll(
                    () -> assertThat(categoryRepository.fetchById(category1.getId()).getName()).isEqualTo(
                            updateCategoryName),
                    () -> assertThat(categoryRepository.fetchById(category1.getId()).getOrdinal()).isEqualTo(2L),
                    () -> assertThat(categoryRepository.fetchById(4L).getName()).isEqualTo(
                            createCategoryName),
                    () -> assertThat(categoryRepository.fetchById(4L).getOrdinal()).isEqualTo(1L)
            );

        }

        @Test
        @DisplayName("카테고리 편집 실패: 수정 권한 없음")
        void updateCategoriesFailWithUnauthorizedUpdate() {
            Category category = categoryRepository.save(new Category("category1", member, 1));
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
            UpdateCategoryRequest request = new UpdateCategoryRequest(category.getId(), "updateName",
                    category.getOrdinal());

            assertThatThrownBy(
                    () -> sut.updateCategories(otherMember, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(request),
                            List.of())))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }

        @Test
        @DisplayName("카테고리 편집 실패: 기본 카테고리 수정")
        void updateCategoriesFailWithDefaultCategory() {
            categoryRepository.save(new Category("category1", member, 1));

            UpdateCategoryRequest request = new UpdateCategoryRequest(defaultCategory.getId(), "updateName", 1);

            assertThatThrownBy(
                    () -> sut.updateCategories(member, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(request),
                            List.of())))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("기본 카테고리는 수정 및 삭제할 수 없습니다.");
        }


        @Test
        @DisplayName("카테고리 편집 실패: 중복된 카테고리 이름")
        void duplicatedCategoryName() {
            String duplicatedName = "duplicatedName";
            Category category = categoryRepository.save(new Category(duplicatedName, member, 1));

            CreateCategoryRequest createRequest = new CreateCategoryRequest(duplicatedName, 2);
            UpdateCategoryRequest updateRequest = new UpdateCategoryRequest(category.getId(), category.getName(),
                    category.getOrdinal());

            assertThatThrownBy(
                    () -> sut.updateCategories(member, new UpdateAllCategoriesRequest(
                            List.of(createRequest),
                            List.of(updateRequest),
                            List.of())))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("요청에 중복된 카테고리 이름이 존재합니다.");
        }

        @Test
        @DisplayName("카테고리 편집 실패: 존재하지 않는 카테고리 수정")
        void notSavedCategoryId() {
            long notSavedId = 100L;
            UpdateCategoryRequest request = new UpdateCategoryRequest(notSavedId, "categoryName", 1);

            assertThatThrownBy(
                    () -> sut.updateCategories(member, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(request),
                            List.of())))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카테고리 편집 실패: 삭제 권한 없음")
        void updateCategoriesFailWithUnauthorizedDelete() {
            categoryRepository.save(new Category("category1", member, 1));
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));

            assertThatThrownBy(
                    () -> sut.updateCategories(otherMember, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(),
                            List.of(1L))))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }

        @Test
        @DisplayName("카테고리 편집 실패: 존재하지 않는 카테고리 삭제")
        void deleteCategoryFailWithNotExistCategory() {
            long notSavedId = 100L;

            assertThatThrownBy(
                    () -> sut.updateCategories(member, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(),
                            List.of(notSavedId))))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카테고리 편집 실패: 템플릿이 존재하는 카테고리 삭제")
        void deleteByIdFailExistsTemplate() {
            Category category = categoryRepository.save(new Category("카테고리 1", member, 1));
            templateRepository.save(new Template(member, "title", "desciption", category));

            assertThatThrownBy(
                    () -> sut.updateCategories(member, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(),
                            List.of(category.getId()))))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }

        @Test
        @DisplayName("카테고리 편집 실패: 기본 카테고리 삭제")
        void deleteByIdFailDefaultCategory() {
            categoryRepository.save(new Category("카테고리 1", member, 1));

            assertThatThrownBy(
                    () -> sut.updateCategories(member, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(),
                            List.of(defaultCategory.getId()))))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("기본 카테고리는 수정 및 삭제할 수 없습니다.");
        }

        @Test
        @DisplayName("카테고리 편집 실패: 중복된 id 수정 및 삭제")
        void deleteByIdFailDuplicatedId() {
            Category category = categoryRepository.save(new Category("카테고리 1", member, 1));
            UpdateCategoryRequest request = new UpdateCategoryRequest(category.getId(), category.getName(), 1);

            assertThatThrownBy(
                    () -> sut.updateCategories(member, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(request),
                            List.of(category.getId()))))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("요청에 중복된 id가 존재합니다.");
        }

        @Test
        @DisplayName("카테고리 편집 실패: 잘못된 개수의 카테고리")
        void deleteByIdFailWrongCount() {
            Category category1 = categoryRepository.save(new Category("카테고리 1", member, 1));
            categoryRepository.save(new Category("카테고리 2", member, 2));
            UpdateCategoryRequest request = new UpdateCategoryRequest(category1.getId(), category1.getName(), 1);

            assertThatThrownBy(
                    () -> sut.updateCategories(member, new UpdateAllCategoriesRequest(
                            List.of(),
                            List.of(request),
                            List.of())))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("카테고리의 개수가 일치하지 않습니다.");
        }
    }
}
