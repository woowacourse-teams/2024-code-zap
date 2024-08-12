package codezap.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.category.dto.request.CreateCategoryRequest;
import codezap.category.dto.request.UpdateCategoryRequest;
import codezap.category.dto.response.FindAllCategoriesResponse;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.fixture.MemberDtoFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.TemplateRepository;

class CategoryServiceTest {

    private Member firstMember = new Member(1L, "test1@email.com", "password1234", "username1");
    private Member secondMember = new Member(2L, "test2@email.com", "password1234", "username2");

    private CategoryRepository categoryRepository = new FakeCategoryRepository();

    private TemplateRepository templateRepository = new FakeTemplateRepository();

    private MemberRepository memberRepository = new FakeMemberRepository(List.of(firstMember, secondMember));

    private CategoryService categoryService = new CategoryService(categoryRepository, templateRepository,
            memberRepository);

    @Nested
    @DisplayName("카테고리 생성 테스트")
    class createCategoryTest {
        @Test
        @DisplayName("카테고리 생성 성공")
        void createCategorySuccess() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category1");

            Long categoryId = categoryService.create(createCategoryRequest, memberDto);

            assertThat(categoryId).isEqualTo(1L);
        }

        @Test
        @DisplayName("카테고리 생성 실패: 동일한 멤버, 중복된 이름의 카테고리 이름 생성")
        void createCategoryFailWithSameMemberAndDuplicateName() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            categoryRepository.save(new Category("category", member));
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category");

            assertThatThrownBy(() -> categoryService.create(createCategoryRequest, memberDto))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("이름이 " + createCategoryRequest.name() + "인 카테고리가 이미 존재합니다.");
        }

        @Test
        @DisplayName("카테고리 생성 성공: 다른 멤버, 중복된 이름의 카테고리 이름 생성")
        void createCategorySuccessWithOtherMemberAndSameName() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            categoryRepository.save(new Category("category", member));
            CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest("category");

            MemberDto otherMemberDto = MemberDtoFixture.getSecondMemberDto();
            Long categoryId = categoryService.create(createCategoryRequest, otherMemberDto);

            assertThat(categoryId).isEqualTo(2L);
        }
    }

    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    void findAllCategoriesSuccess() {
        Member member = memberRepository.fetchById(1L);
        categoryRepository.save(new Category("category1", member));
        categoryRepository.save(new Category("category2", member));

        FindAllCategoriesResponse findAllCategoriesResponse = categoryService.findAll();

        assertThat(findAllCategoriesResponse.categories()).hasSize(2);
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void updateCategorySuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        Category savedCategory = categoryRepository.save(new Category("category1", member));

        // when
        categoryService.update(savedCategory.getId(), new UpdateCategoryRequest("updateName"), memberDto);

        // then
        assertThat(categoryRepository.fetchById(savedCategory.getId()).getName()).isEqualTo("updateName");
    }

    @Test
    @DisplayName("카테고리 수정 실패: 권한 없음")
    void updateCategoryFailWithUnauthorized() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        Category savedCategory = categoryRepository.save(new Category("category1", member));

        // when
        MemberDto otherMemberDto = MemberDtoFixture.getSecondMemberDto();

        // then
        assertThatCode(() -> categoryService.update(savedCategory.getId(), new UpdateCategoryRequest("updateName"), otherMemberDto))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategorySuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        categoryRepository.save(new Category("category1", member));
        Category savedCategory = categoryRepository.save(new Category("category1", member));

        // when
        categoryService.deleteById(savedCategory.getId(), memberDto);

        // then
        assertThat(categoryRepository.findAllByMemberOrderById(member)).hasSize(1);
    }

    @Test
    @DisplayName("카테고리 삭제 실패")
    void deleteCategoryFailWithUnauthorized() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        categoryRepository.save(new Category("category1", member));
        Category savedCategory = categoryRepository.save(new Category("category1", member));

        // when
        MemberDto otherMemberDto = MemberDtoFixture.getSecondMemberDto();

        // then
        assertThatCode(() -> categoryService.deleteById(savedCategory.getId(), otherMemberDto))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
    }
}
