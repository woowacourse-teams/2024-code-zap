package codezap.template.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.SourceCodeFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.exception.CodeZapException;
import codezap.global.repository.RepositoryTest;
import codezap.member.domain.Member;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

@RepositoryTest
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
    @DisplayName("템플릿 목록으로 썸네일 목록 조회")
    class FindAllByTemplateIn {

        @Test
        @DisplayName("템플릿 목록으로 썸네일 목록 조회 성공")
        void findAllByTemplateIn() {
            // given
            var template1 = createSavedTemplate();
            var sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template1, 1));
            var thumbnail1 = sut.save(new Thumbnail(template1, sourceCode1));

            var template2 = createSecondTemplate();
            var sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template2, 1));
            var thumbnail2 = sut.save(new Thumbnail(template2, sourceCode2));

            // when
            var actual = sut.findAllByTemplateIn(List.of(template1.getId(), template2.getId()));

            // then
            assertThat(actual).containsExactlyInAnyOrder(thumbnail1, thumbnail2);
        }

        @Test
        @DisplayName("템플릿 목록으로 썸네일 목록 조회 성공: 해당하는 썸네일이 없는 경우 빈 목록 반환")
        void findAllByTemplateInWhenNotExistThumbnail() {
            // given
            var template1 = createSavedTemplate();

            assertThat(sut.findAllByTemplateIn(List.of(template1.getId()))).isEmpty();
        }

        private Template createSavedTemplate() {
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            return templateRepository.save(TemplateFixture.get(member, category));
        }

        private Template createSecondTemplate() {
            Member member = memberRepository.save(MemberFixture.getSecondMember());
            Category category = categoryRepository.save(CategoryFixture.getSecondCategory());
            return templateRepository.save(TemplateFixture.get(member, category));
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
            sut.deleteAllByTemplateIds(List.of(template.getId()));

            // then
            assertThatThrownBy(() -> sut.fetchByTemplate(template))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자가 " + template.getId() + "인 템플릿에 해당하는 썸네일이 없습니다.");
        }

        @Test
        @DisplayName("템플릿 id로 썸네일 삭제 성공: 존재하지 않는 템플릿의 id로 삭제해도 예외로 처리하지 않는다.")
        void deleteByNotExistTemplateId() {
            assertThatCode(() -> sut.deleteAllByTemplateIds(List.of(100L)))
                    .doesNotThrowAnyException();
        }
    }
}
