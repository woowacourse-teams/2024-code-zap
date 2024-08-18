package codezap.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.TemplateRepository;

class CategoryTemplateServiceTest {
    private final CategoryRepository categoryRepository = new FakeCategoryRepository();
    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final MemberRepository memberRepository = new FakeMemberRepository();

    private final CategoryTemplateService categoryService =
            new CategoryTemplateService(categoryRepository, templateRepository);

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategorySuccess() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        categoryRepository.save(new Category("category1", member));
        Category savedCategory = categoryRepository.save(new Category("category1", member));
        int beforeDeleteSize = categoryRepository.findAllByMemberOrderById(member).size();

        categoryService.deleteById(member, savedCategory.getId());

        assertThat(categoryRepository.findAllByMemberOrderById(member)).hasSize(beforeDeleteSize - 1);
    }

    @Test
    @DisplayName("카테고리 삭제 실패: 권한 없음")
    void deleteCategoryFailWithUnauthorized() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
        Category savedCategory = categoryRepository.save(new Category("category1", member));

        assertThatCode(() -> categoryService.deleteById(otherMember, savedCategory.getId()))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
    }

    @Test
    @DisplayName("카테고리 삭제 실패: 이미 없는 카테고리")
    void deleteCategoryFailWithNotExistCateory() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        Category category = categoryRepository.save(new Category("category1", member));

        categoryService.deleteById(member, category.getId());

        assertThatCode(() -> categoryService.deleteById(member, category.getId()))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("식별자 " + category.getId() + "에 해당하는 카테고리가 존재하지 않습니다.");
    }
}
