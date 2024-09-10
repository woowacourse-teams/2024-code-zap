package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

@JpaRepositoryTest
public class ThumbnailRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private SourceCodeRepository sourceCodeRepository;

    @Autowired
    private ThumbnailRepository sut;

    @Nested
    @DisplayName("id로 썸네일 조회")
    class FetchById {

        @Test
        @DisplayName("성공: id로 썸네일 조회")
        void fetchByIdSuccess() {
            // given
            var member = new Member("Zappy", "password", "salt");
            memberRepository.save(member);
            var category = Category.createDefaultCategory(member);
            categoryRepository.save(category);
            var template = new Template(member, "Template Title", "Description", category);
            templateRepository.save(template);
            var sourceCode = new SourceCode(template, "filename", "content", 1);
            sourceCodeRepository.save(sourceCode);
            var thumbnail = new Thumbnail(template, sourceCode);
            sut.save(thumbnail);

            // when
            var actual = sut.fetchById(thumbnail.getId());

            // then
            assertThat(actual).isEqualTo(thumbnail);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 id로 썸네일 조회")
        void fetchByIdFail() {
            var id = 100L;

            assertThatThrownBy(() -> sut.fetchById(id))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + id + "에 해당하는 썸네일이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿으로 썸네일 조회")
    class FetchByTemplate {

        @Test
        @DisplayName("성공: 템플릿으로 썸네일 조회")
        void fetchByTemplateSuccess() {
            // given
            var member = new Member("Zappy", "password", "salt");
            memberRepository.save(member);
            var category = Category.createDefaultCategory(member);
            categoryRepository.save(category);
            var template = new Template(member, "Template Title", "Description", category);
            templateRepository.save(template);
            var sourceCode = new SourceCode(template, "filename", "content", 1);
            sourceCodeRepository.save(sourceCode);
            var thumbnail = new Thumbnail(template, sourceCode);

            // when
            sut.save(thumbnail);

            // then
            var actual = sut.fetchByTemplate(template);
            assertThat(actual).isEqualTo(thumbnail);
        }
    }

    @Nested
    @DisplayName("템플릿 id로 썸네일 삭제")
    class DeleteByTemplateId {

        @Test
        @DisplayName("성공: 템플릿 id로 썸네일 삭제")
        void deleteByTemplateIdSuccess() {
            // given
            var member = new Member("Zappy", "password", "salt");
            memberRepository.save(member);
            var category = Category.createDefaultCategory(member);
            categoryRepository.save(category);
            var template = new Template(member, "Template Title", "Description", category);
            templateRepository.save(template);
            var sourceCode = new SourceCode(template, "filename", "content", 1);
            sourceCodeRepository.save(sourceCode);
            var thumbnail = new Thumbnail(template, sourceCode);
            sut.save(thumbnail);

            // when
            sut.deleteByTemplateId(template.getId());

            // then
            assertThatThrownBy(() -> sut.fetchById(template.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + 1 + "에 해당하는 썸네일이 존재하지 않습니다.");
        }
    }
}
