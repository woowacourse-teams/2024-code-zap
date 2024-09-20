package codezap.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.DatabaseIsolation;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.MemberRepository;

@SpringBootTest
@DatabaseIsolation
class CategoryTemplateServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryTemplateService categoryTemplateService;

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategorySuccess() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        Category savedCategory = categoryRepository.save(new Category("category1", member));
        int beforeDeleteSize = categoryRepository.findAllByMemberOrderById(member).size();

        categoryTemplateService.deleteById(member, savedCategory.getId());

        assertThat(categoryRepository.findAllByMemberOrderById(member)).hasSize(beforeDeleteSize - 1);
    }

    @Test
    @DisplayName("카테고리 삭제 실패: 권한 없음")
    void deleteCategoryFailWithUnauthorized() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
        Category savedCategory = categoryRepository.save(new Category("category1", member));

        assertThatCode(() -> categoryTemplateService.deleteById(otherMember, savedCategory.getId()))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
    }

    @Test
    @DisplayName("카테고리 삭제 실패: 이미 없는 카테고리")
    void deleteCategoryFailWithNotExistCategory() {
        Member member = memberRepository.save(MemberFixture.memberFixture());
        Category category = categoryRepository.save(new Category("category1", member));

        categoryTemplateService.deleteById(member, category.getId());

        assertThatCode(() -> categoryTemplateService.deleteById(member, category.getId()))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("식별자 " + category.getId() + "에 해당하는 카테고리가 존재하지 않습니다.");
    }
}
