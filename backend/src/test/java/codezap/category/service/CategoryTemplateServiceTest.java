package codezap.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import codezap.template.domain.Template;
import codezap.template.repository.TemplateRepository;

@SpringBootTest
@DatabaseIsolation
class CategoryTemplateServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private CategoryTemplateService sut;

    @Nested
    @DisplayName("카테고리 삭제 테스트")
    class DeleteById {

        @Test
        @DisplayName("카테고리 삭제 성공")
        void deleteCategorySuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category savedCategory = categoryRepository.save(new Category("category1", member));
            int beforeDeleteSize = categoryRepository.findAllByMemberOrderById(member).size();

            sut.deleteById(member, savedCategory.getId());

            assertAll(
                    () -> assertThat(categoryRepository.findAllByMemberOrderById(member))
                            .hasSize(beforeDeleteSize - 1),
                    () -> assertThat(categoryRepository.existsById(savedCategory.getId()))
                            .isFalse()
            );
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 권한 없음")
        void deleteCategoryFailWithUnauthorized() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Member otherMember = memberRepository.save(MemberFixture.createFixture("otherMember"));
            Category savedCategory = categoryRepository.save(new Category("category1", member));

            assertThatCode(() -> sut.deleteById(otherMember, savedCategory.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }

        @Test
        @DisplayName("카테고리 삭제 실패: 존재하지 않는 카테고리는 삭제할 수 없음")
        void deleteCategoryFailWithNotExistCategory() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            long notSavedCategoryId = 100L;

            assertThatCode(() -> sut.deleteById(member, notSavedCategoryId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notSavedCategoryId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카테고리 아이디로 카테고리 삭제 실패 : 해당 카테고리에 속한 템플릿이 존재하면 삭제할 수 없음")
        void deleteByIdFailExistsTemplate() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category category = categoryRepository.save(new Category("카테고리 1", member));
            templateRepository.save(new Template(member, "title", "desciption", category));

            assertThatThrownBy(() -> sut.deleteById(member, category.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }

        @Test
        @DisplayName("카테고리 아이디로 카테고리 삭제 실패 : 기본 카테고리는 삭제할 수 없음")
        void deleteByIdFailDefaultCategory() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category defaultCategory = categoryRepository.save(Category.createDefaultCategory(member));

            assertThatThrownBy(() -> sut.deleteById(member, defaultCategory.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("기본 카테고리는 삭제할 수 없습니다.");
        }
    }
}
