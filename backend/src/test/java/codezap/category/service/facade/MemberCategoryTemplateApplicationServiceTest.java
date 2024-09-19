package codezap.category.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import codezap.member.dto.MemberDto;
import codezap.member.fixture.MemberFixture;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Template;
import codezap.template.repository.TemplateRepository;

@SpringBootTest
@DatabaseIsolation
class MemberCategoryTemplateApplicationServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private MemberCategoryTemplateApplicationService sut;

    @Nested
    @DisplayName("카테고리 아이디로 카테고리 삭제")
    class DeleteById {
        @Test
        @DisplayName("카테고리 아이디로 카테고리 삭제 성공")
        void deleteByIdSuccess() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category category = categoryRepository.save(new Category("카테고리 1", member));
            Long categoryId = category.getId();

            sut.deleteById(MemberDto.from(member), categoryId);

            boolean actual = categoryRepository.existsById(categoryId);
            assertThat(actual).isFalse();
        }

        @Test
        @DisplayName("카테고리 아이디로 카테고리 삭제 실패 : 카테고리 존재하지 않음")
        void deleteByIdFailNotExistsCategory() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Long notExistsId = 100L;

            assertThatThrownBy(() -> sut.deleteById(MemberDto.from(member), notExistsId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistsId + "에 해당하는 카테고리가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카테고리 아이디로 카테고리 삭제 실패 : 멤버 존재하지 않음")
        void deleteByIdFailNotExistsMember() {
            MemberDto notExistsMemberDto = MemberDto.from(MemberFixture.memberFixture());

            assertThatThrownBy(() -> sut.deleteById(notExistsMemberDto, 1L))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistsMemberDto.id() + "에 해당하는 멤버가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("카테고리 아이디로 카테고리 삭제 실패 : 카테고리를 삭제할 권한이 없음")
        void deleteByIdFailNoPermission() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Member otherMember = memberRepository.save(MemberFixture.createFixture("켬미"));
            Category category = categoryRepository.save(new Category("카테고리 1", otherMember));

            assertThatThrownBy(() -> sut.deleteById(MemberDto.from(member), category.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리를 수정 또는 삭제할 권한이 없는 유저입니다.");
        }

        @Test
        @DisplayName("카테고리 아이디로 카테고리 삭제 실패 : 해당 카테고리에 속한 템플릿이 존재하면 삭제할 수 없음")
        void deleteByIdFailExistsTemplate() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category category = categoryRepository.save(new Category("카테고리 1", member));
            templateRepository.save(new Template(member, "title", "desciption", category));

            assertThatThrownBy(() -> sut.deleteById(MemberDto.from(member), category.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿이 존재하는 카테고리는 삭제할 수 없습니다.");
        }

        @Test
        @DisplayName("카테고리 아이디로 카테고리 삭제 실패 : 기본 카테고리는 삭제할 수 없음")
        void deleteByIdFailDefaultCategory() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            Category defaultCategory = categoryRepository.save(Category.createDefaultCategory(member));

            assertThatThrownBy(() -> sut.deleteById(MemberDto.from(member), defaultCategory.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("기본 카테고리는 삭제할 수 없습니다.");
        }
    }
}
