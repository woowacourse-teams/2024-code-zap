package codezap.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.TemplateRepository;

class CategoryServiceTest {

    private final CategoryRepository categoryRepository = new FakeCategoryRepository();
    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final MemberRepository memberRepository = new FakeMemberRepository();

    private final CategoryService categoryService = new CategoryService(categoryRepository);

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class createCategoryTest {

        @Test
        @DisplayName("카테고리 생성 성공")
        void createCategorySuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category1");

            Long categoryId = categoryService.create(member, createCategoryRequest);

            assertThat(categoryId).isEqualTo(1L);
        }

        @Test
        @DisplayName("카테고리 생성 실패: 동일한 멤버, 중복된 이름의 카테고리 이름 생성")
        void createCategoryFailWithSameMemberAndDuplicateName() {
            String duplicatedCategoryName = "category";
            Member member = memberRepository.save(MemberFixture.memberFixture());
            categoryRepository.save(new Category(duplicatedCategoryName, member));

            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(duplicatedCategoryName);

            assertThatThrownBy(
                    () -> categoryService.create(member, createCategoryRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + duplicatedCategoryName + "인 카테고리가 이미 존재합니다.");
        }

        @Test
        @DisplayName("카테고리 생성 성공: 다른 멤버, 중복된 이름의 카테고리 이름 생성")
        void createCategorySuccessWithOtherMemberAndSameName() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            categoryRepository.save(new Category("category", member));
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));

            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category");

            assertThat(categoryService.create(otherMember, createCategoryRequest)).isEqualTo(2L);
        }
    }

    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    void findAllCategoriesSuccess() {
        Member member = memberRepository.save(MemberFixture.memberFixture());

        categoryRepository.save(new Category("category1", member));
        categoryRepository.save(new Category("category2", member));

        FindAllCategoriesResponse findAllCategoriesResponse = categoryService.findAll();

        assertThat(findAllCategoriesResponse.categories()).hasSize(2);
    }

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

        assertThatCode(
                () -> categoryService.update(otherMember, savedCategory.getId(),
                        new UpdateCategoryRequest("updateName")))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
    }

}
