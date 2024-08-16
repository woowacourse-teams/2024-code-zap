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
import codezap.member.dto.MemberDto;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.TemplateRepository;

class CategoryServiceTest {

    private CategoryRepository categoryRepository = new FakeCategoryRepository();
    private TemplateRepository templateRepository = new FakeTemplateRepository();
    private MemberRepository memberRepository = new FakeMemberRepository();

    private CategoryService categoryService = new CategoryService(categoryRepository, templateRepository,
            memberRepository);

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class createCategoryTest {

        @Test
        @DisplayName("카테고리 생성 성공")
        void createCategorySuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category1");

            Long categoryId = categoryService.create(MemberDto.from(member), createCategoryRequest);

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
                    () -> categoryService.create(MemberDto.from(member), createCategoryRequest)).isInstanceOf(
                    CodeZapException.class).hasMessage("이름이 " + duplicatedCategoryName + "인 카테고리가 이미 존재합니다.");
        }

        @Test
        @DisplayName("카테고리 생성 성공: 다른 멤버, 중복된 이름의 카테고리 이름 생성")
        void createCategorySuccessWithOtherMemberAndSameName() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            categoryRepository.save(new Category("category", member));
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));

            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category");

            assertThat(categoryService.create(MemberDto.from(otherMember), createCategoryRequest)).isEqualTo(2L);
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

        categoryService.update(MemberDto.from(member), savedCategory.getId(),
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
                () -> categoryService.update(MemberDto.from(otherMember), savedCategory.getId(),
                        new UpdateCategoryRequest("updateName")))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategorySuccess() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        categoryRepository.save(new Category("category1", member));
        Category savedCategory = categoryRepository.save(new Category("category1", member));
        int beforeDeleteSize = categoryRepository.findAllByMemberOrderById(member).size();

        categoryService.deleteById(MemberDto.from(member), savedCategory.getId());

        assertThat(categoryRepository.findAllByMemberOrderById(member)).hasSize(beforeDeleteSize - 1);
    }

    @Test
    @DisplayName("카테고리 삭제 실패: 권한 없음")
    void deleteCategoryFailWithUnauthorized() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
        Category savedCategory = categoryRepository.save(new Category("category1", member));

        assertThatCode(() -> categoryService.deleteById(MemberDto.from(otherMember), savedCategory.getId()))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
    }

    @Test
    @DisplayName("카테고리 삭제 실패: 이미 없는 카테고리")
    void deleteCategoryFailWithNotExistCateory() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        Category category = categoryRepository.save(new Category("category1", member));

        categoryService.deleteById(MemberDto.from(member), category.getId());

        assertThatCode(() -> categoryService.deleteById(MemberDto.from(member), category.getId()))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("식별자 " + category.getId() + "에 해당하는 카테고리가 존재하지 않습니다.");
    }
}
