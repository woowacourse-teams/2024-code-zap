package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import codezap.category.repository.CategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.DatabaseIsolation;
import codezap.global.exception.CodeZapException;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Thumbnail;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailRepository;

@SpringBootTest
@DatabaseIsolation
class ThumbnailServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private SourceCodeRepository sourceCodeRepository;

    @Autowired
    private ThumbnailRepository thumbnailRepository;

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
            var actual = thumbnailRepository.findAll();

            assertThat(actual).hasSize(1);
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

            assertThat(actual.getTemplate()).isEqualTo(template);
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
    @DisplayName("썸네일 삭제")
    class DeleteByTemplateIDs {

        @Test
        @DisplayName("썸네일 삭제 성공")
        void deleteByTemplateSuccess() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template1 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode1 = sourceCodeRepository.save(new SourceCode(template1, "Filename 1", "Content 1", 1));
            thumbnailRepository.save(new Thumbnail(template1, sourceCode1));
            var template2 = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode2 = sourceCodeRepository.save(new SourceCode(template2, "Filename 2", "Content 2", 1));
            thumbnailRepository.save(new Thumbnail(template2, sourceCode2));

            sut.deleteByTemplateIds(List.of(template1.getId()));
            var actual = thumbnailRepository.findAll();

            assertAll(
                    () -> assertThat(actual).hasSize(1),
                    () -> assertThat(actual.get(0).getTemplate()).isEqualTo(template2)
            );
        }

        @Test
        @DisplayName("썸네일 삭제 성공: 존재하지 않는 경우")
        void deleteByTemplateFailWithWrongID() {
            var member = memberRepository.save(MemberFixture.getFirstMember());
            var category = categoryRepository.save(CategoryFixture.getFirstCategory());
            var template = templateRepository.save(TemplateFixture.get(member, category));
            var sourceCode = sourceCodeRepository.save(new SourceCode(template, "Filename 1", "Content 1", 1));
            thumbnailRepository.save(new Thumbnail(template, sourceCode));
            var nonExistentID = 100L;

            sut.deleteByTemplateIds(List.of(nonExistentID));
            var actual = thumbnailRepository.findAll();

            assertAll(
                    () -> assertThat(actual).hasSize(1),
                    () -> assertThat(actual.get(0).getTemplate()).isEqualTo(template)
            );
        }
    }
}
