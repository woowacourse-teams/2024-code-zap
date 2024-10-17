package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.SourceCodeFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.ServiceTest;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;

class ThumbnailServiceTest extends ServiceTest {

    @Autowired
    private ThumbnailService sut;

    @Nested
    @DisplayName("썸네일 생성")
    class CreateThumbnail {

        @Test
        @DisplayName("썸네일 생성 성공")
        void createThumbnailSuccess() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode = sourceCodeRepository.save(new SourceCode(template, "Filename 1", "Content 1", 1));

            sut.createThumbnail(template, sourceCode);
            var actual = thumbnailRepository.fetchByTemplate(template);

            assertAll(
                    () -> assertThat(actual.getId()).isEqualTo(1L),
                    () -> assertThat(actual.getTemplate()).isEqualTo(template),
                    () -> assertThat(actual.getSourceCode()).isEqualTo(sourceCode)
            );
        }
    }

    @Nested
    @DisplayName("썸네일 조회")
    class GetByTemplate {

        @Test
        @DisplayName("썸네일 조회 성공")
        void getByTemplateSuccess() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode = sourceCodeRepository.save(new SourceCode(template, "Filename 1", "Content 1", 1));
            thumbnailRepository.save(new Thumbnail(template, sourceCode));

            var actual = sut.getByTemplate(template);

            assertAll(
                    () -> assertThat(actual.getId()).isEqualTo(sourceCode.getId()),
                    () -> assertThat(actual.getTemplate()).isEqualTo(template),
                    () -> assertThat(actual.getSourceCode()).isEqualTo(sourceCode)
            );
        }

        @Test
        @DisplayName("썸네일 조회 실패: 해당하는 썸네일이 없는 경우")
        void getByTemplateFailWithWrongID() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(member, category));

            assertThatThrownBy(() -> sut.getByTemplate(template))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자가 " + template.getId() + "인 템플릿에 해당하는 썸네일이 없습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 목록으로부터 썸네일 목록 조회")
    class GetAllByTemplates {

        @Test
        @DisplayName("템플릿 목록으로부터 썸네일 목록 조회 성공")
        void getAllByTemplates() {
            var template1 = createSavedTemplate();
            var sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template1, 1));
            var thumbnail1 = thumbnailRepository.save(new Thumbnail(template1, sourceCode1));

            var template2 = createSecondTemplate();
            var sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template2, 1));
            var thumbnail2 = thumbnailRepository.save(new Thumbnail(template2, sourceCode2));

            var actual = sut.getAllByTemplates(List.of(template1, template2));

           assertThat(actual).containsExactlyInAnyOrder(thumbnail1, thumbnail2);
        }

        @Test
        @DisplayName("템플릿 목록으로부터 썸네일 목록 조회: 해당하는 썸네일이 없는 경우 빈목록 반환")
        void getAllByTemplatesNot() {
            var template1 = createSavedTemplate();

            assertThat(sut.getAllByTemplates(List.of())).isEmpty();
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
    @DisplayName("썸네일 삭제")
    class DeleteByTemplateIDs {

        @Test
        @DisplayName("썸네일 삭제 성공: 1개의 썸네일 삭제")
        void deleteByTemplateSuccessWithOneThumbnail() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template1 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "Filename 1", "Content 1", 1));
            var savedThumbnail1 = thumbnailRepository.save(new Thumbnail(template1, sourceCode1));
            var template2 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode2 = sourceCodeRepository.save(new SourceCode(template2, "Filename 2", "Content 2", 1));
            var savedThumbnail2 = thumbnailRepository.save(new Thumbnail(template2, sourceCode2));

            sut.deleteByTemplateIds(List.of(template1.getId()));
            var actual = thumbnailRepository.findAll();

            assertThat(actual).hasSize(1)
                    .containsExactly(savedThumbnail2)
                    .doesNotContain(savedThumbnail1);
        }

        @Test
        @DisplayName("썸네일 삭제 성공: 2개의 썸네일 삭제")
        void deleteByTemplateSuccessWithTwoThumbnail() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template1 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "Filename 1", "Content 1", 1));
            var savedThumbnail1 = thumbnailRepository.save(new Thumbnail(template1, sourceCode1));
            var template2 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode2 = sourceCodeRepository.save(new SourceCode(template2, "Filename 2", "Content 2", 1));
            var savedThumbnail2 = thumbnailRepository.save(new Thumbnail(template2, sourceCode2));

            sut.deleteByTemplateIds(List.of(template1.getId(), template2.getId()));
            var actual = thumbnailRepository.findAll();

            assertThat(actual).doesNotContain(savedThumbnail1, savedThumbnail2);
        }

        @Test
        @DisplayName("썸네일 삭제 성공: 존재하지 않는 경우")
        void deleteByTemplateFailWithWrongID() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode = sourceCodeRepository.save(new SourceCode(template, "Filename 1", "Content 1", 1));
            var savedThumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode));
            var nonExistentID = 100L;

            sut.deleteByTemplateIds(List.of(nonExistentID));
            var actual = thumbnailRepository.findAll();

            assertThat(actual).hasSize(1)
                    .containsExactly(savedThumbnail);
        }
    }
}
