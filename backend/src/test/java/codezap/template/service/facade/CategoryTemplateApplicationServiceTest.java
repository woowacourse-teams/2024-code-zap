package codezap.template.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import codezap.category.domain.Category;
import codezap.fixture.MemberFixture;
import codezap.fixture.TemplateFixture;
import codezap.global.ServiceTest;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;

@Transactional
class CategoryTemplateApplicationServiceTest extends ServiceTest {

    @Autowired
    private CategoryTemplateApplicationService categoryTemplateApplicationService;

    @Nested
    @DisplayName("템플릿 생성")
    class CreateTemplate {

        @Test
        @DisplayName("성공: 작성자의 권한 확인 후 템플릿 생성")
        void createTemplate_Success() {
            // given
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category membersCategory = categoryRepository.save(new Category("Members", member));
            List<CreateSourceCodeRequest> sourceCodeRequests = List.of(
                    new CreateSourceCodeRequest("filename", "content", 1));
            CreateTemplateRequest request = new CreateTemplateRequest(
                    "Template", "Description", sourceCodeRequests, 1, membersCategory.getId(), List.of("태그"));

            // when
            Long result = categoryTemplateApplicationService.createTemplate(member, request);

            // then
            assertThat(result).isEqualTo(1L);
        }

        @Test
        @DisplayName("실패: 카테고리에 대한 권한이 없는 경우")
        void createTemplate_Fail_NoAuthorization() {
            // given
            Member ownerMember = memberRepository.save(MemberFixture.getFirstMember());
            Member otherMember = memberRepository.save(MemberFixture.getSecondMember());
            Category category = categoryRepository.save(new Category("Members", ownerMember));
            List<CreateSourceCodeRequest> sourceCodeRequests = List.of(
                    new CreateSourceCodeRequest("filename", "content", 1));
            CreateTemplateRequest request = new CreateTemplateRequest(
                    "Template", "Description", sourceCodeRequests, 1, category.getId(), List.of("태그"));

            // when & then
            assertThatThrownBy(() -> categoryTemplateApplicationService.createTemplate(otherMember, request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리에 대한 권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 수정")
    class UpdateTemplate {

        @Test
        @DisplayName("성공")
        void updateTemplate_Success() {
            // given
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(new Category("Members", member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));
            SourceCode sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename", "content", 1));
            thumbnailRepository.save(new Thumbnail(template, sourceCode));
            UpdateSourceCodeRequest updateSourceCodeRequest = new UpdateSourceCodeRequest(
                    sourceCode.getId(), "Updated filename", "Updated content", 1);
            UpdateTemplateRequest request = new UpdateTemplateRequest("Updated Template", "Updated Description", Collections.emptyList(),
                    List.of(updateSourceCodeRequest), Collections.emptyList(), category.getId(), Collections.emptyList());

            // when
            categoryTemplateApplicationService.update(member, template.getId(), request);

            // then
            Template updatedTemplate = templateRepository.fetchById(template.getId());
            assertAll(
                    () -> assertEquals("Updated Template", updatedTemplate.getTitle()),
                    () -> assertEquals("Updated Description", updatedTemplate.getDescription())
            );
        }

        @Test
        @DisplayName("실패: 카테고리에 대한 권한이 없는 경우")
        void updateTemplate_WhenNoAuthorization() {
            // given
            Member otherMember = memberRepository.save(MemberFixture.getFirstMember());
            Category othersCategory = categoryRepository.save(new Category("Members", otherMember));

            Member member = memberRepository.save(MemberFixture.getSecondMember());
            Category category = categoryRepository.save(new Category("Members", member));
            Template template = templateRepository.save(TemplateFixture.get(member, category));
            SourceCode sourceCode = sourceCodeRepository.save(new SourceCode(template, "filename", "content", 1));
            thumbnailRepository.save(new Thumbnail(template, sourceCode));
            UpdateSourceCodeRequest updateSourceCodeRequest = new UpdateSourceCodeRequest(
                    sourceCode.getId(), "Updated filename", "Updated content", 1);
            UpdateTemplateRequest request = new UpdateTemplateRequest("Updated Template", "Updated Description", Collections.emptyList(),
                    List.of(updateSourceCodeRequest), Collections.emptyList(), othersCategory.getId(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> categoryTemplateApplicationService.update(otherMember, template.getId(), request))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
        }
    }
}
