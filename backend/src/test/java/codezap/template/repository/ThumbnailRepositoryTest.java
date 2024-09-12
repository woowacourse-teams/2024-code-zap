package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.JpaRepositoryTest;
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
    @DisplayName("썸네일 id로 썸네일 조회")
    class FetchById {

        @Test
        @DisplayName("썸네일 id로 썸네일 조회 성공")
        void fetchByIdSuccess() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(new Template(member, "Template Title", "Description", category));
            var sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename", "content", 1));
            var thumbnail = sut.save(new Thumbnail(template, sourceCode));

            // when
            var actual = sut.fetchById(thumbnail.getId());

            // then
            assertThat(actual).isEqualTo(thumbnail);
        }

        @Test
        @DisplayName("썸네일 id로 썸네일 조회 실패: 존재하지 않는 id")
        void fetchByIdFail() {
            var notExistId = 100L;

            assertThatThrownBy(() -> sut.fetchById(notExistId))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + notExistId + "에 해당하는 썸네일이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿으로 썸네일 조회")
    class FetchByTemplate {

        @Test
        @DisplayName("템플릿으로 썸네일 조회 성공")
        void fetchByTemplateSuccess() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(new Template(member, "Template Title", "Description", category));
            var sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename", "content", 1));
            var thumbnail = sut.save(new Thumbnail(template, sourceCode));

            // when
            var actual = sut.fetchByTemplate(template);

            // then
            assertThat(actual).isEqualTo(thumbnail);
        }
    }

    @Nested
    @DisplayName("템플릿 id로 썸네일 삭제")
    class DeleteByTemplateId {

        @Test
        @DisplayName("템플릿 id로 썸네일 삭제 성공")
        void deleteByTemplateIdSuccess() {
            // given
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(Category.createDefaultCategory(member));
            var template = templateRepository.save(new Template(member, "Template Title", "Description", category));
            var sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename", "content", 1));
            sut.save(new Thumbnail(template, sourceCode));

            // when
            sut.deleteByTemplateId(template.getId());

            // then
            assertThatThrownBy(() -> sut.fetchById(template.getId()))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 " + 1 + "에 해당하는 썸네일이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("템플릿 id로 썸네일 삭제 성공: 존재하지 않는 템플릿의 id로 삭제해도 예외로 처리하지 않는다.")
        void deleteByNotExistTemplateId() {
            assertThatCode(() -> sut.deleteByTemplateId(100L))
                    .doesNotThrowAnyException();
        }
    }
}
