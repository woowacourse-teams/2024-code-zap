package codezap.template.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import codezap.category.domain.Category;
import codezap.category.repository.FakeCategoryRepository;
import codezap.category.service.CategoryService;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.tag.domain.Tag;
import codezap.tag.service.TemplateTagService;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.fixture.TagFixture;
import codezap.template.fixture.TemplateFixture;
import codezap.template.repository.FakeSourceCodeRepository;
import codezap.template.repository.FakeTagRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.FakeTemplateTagRepository;
import codezap.template.repository.FakeThumbnailRepository;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailRepository;
import codezap.template.service.SourceCodeService;
import codezap.template.service.TemplateService;
import codezap.template.service.ThumbnailService;

class TemplateApplicationServiceTest {
    public static final PageRequest PAGEABLE = PageRequest.of(1, 3);
    private final TemplateRepository templateRepository =
            new FakeTemplateRepository(List.of(TemplateFixture.getFirst()));
    private final ThumbnailRepository thumbnailRepository = new FakeThumbnailRepository();
    private final SourceCodeRepository sourceCodeRepository = new FakeSourceCodeRepository();
    public final FakeTemplateTagRepository templateTagRepository = new FakeTemplateTagRepository();
    public final FakeTagRepository tagRepository =
            new FakeTagRepository(List.of(TagFixture.getFirst(), TagFixture.getSecond()));
    public final FakeCategoryRepository categoryRepository = new FakeCategoryRepository(
            List.of(CategoryFixture.getFirstCategory(), CategoryFixture.getSecondCategory())
    );

    private final TemplateService templateService = new TemplateService(templateRepository);

    private final TemplateTagService templateTagService = new TemplateTagService(tagRepository, templateTagRepository);

    private final ThumbnailService thumbnailService = new ThumbnailService(thumbnailRepository);

    private final SourceCodeService sourceCodeService = new SourceCodeService(sourceCodeRepository);

    private final CategoryService categoryService = new CategoryService(categoryRepository);

    private final TagTemplateApplicationService tagTemplateApplicationService =
            new TagTemplateApplicationService(templateTagService, templateService, thumbnailService, sourceCodeService);
    private final TemplateApplicationService templateApplicationService =
            new TemplateApplicationService(categoryService, tagTemplateApplicationService);

    @Nested
    @DisplayName("템플릿 생성")
    class createTemplate {
        @Test
        @DisplayName("템플릿 생성 성공")
        void createTemplateSuccess() {
            // given
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getFirstCategory();
            CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("filename", "content", 1)),
                    1,
                    category.getId(),
                    List.of("태그3", "태그4")
            );

            // when
            Long id = templateApplicationService.createTemplate(member, createTemplateRequest);
            Template template = templateRepository.fetchById(id);

            // then
            assertAll(
                    () -> assertThat(templateRepository.findAll()).hasSize(2),
                    () -> assertThat(template.getTitle()).isEqualTo(createTemplateRequest.title()),
                    () -> assertThat(template.getCategory().getName()).isEqualTo(category.getName())
            );
        }

        @Test
        @DisplayName("템플릿 생성 실패 : 카테고리 없음")
        void createTemplateFailNotCategory() {
            // given
            Member member = MemberFixture.getFirstMember();
            CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("filename", "content", 1)),
                    1,
                    3L,
                    List.of("teg1", "tag2")
            );

            // when & then
            assertThatThrownBy(() -> templateApplicationService.createTemplate(member, createTemplateRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 3에 해당하는 카테고리가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("템플릿 생성 실패 : 카테고리 권한 없음")
        void createTemplateFail() {
            // given
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getSecondCategory();
            CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest(
                    "title",
                    "description",
                    List.of(new CreateSourceCodeRequest("filename", "content", 1)),
                    1,
                    category.getId(),
                    List.of("teg1", "tag2")
            );

            // when & then
            assertThatThrownBy(() -> templateApplicationService.createTemplate(member, createTemplateRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리에 대한 권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 검색")
    class findAllBy {
        @Test
        @DisplayName("템플릿 검색 성공: 카테고리 있음")
        void findAllBySuccess() {
            Member member = MemberFixture.getFirstMember();
            Category category1 = CategoryFixture.getFirstCategory();
            Category category2 = CategoryFixture.getSecondCategory();
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            FindAllTemplatesResponse allBy = templateApplicationService.findAllBy(
                    member.getId(), "hello keyword", category2.getId(), null, PAGEABLE
            );

            assertAll(
                    () -> assertThat(allBy.templates()).hasSize(3),
                    () -> assertThat(allBy.templates()).extracting("id").containsExactly(17L, 18L, 19L),
                    () -> assertThat(allBy.totalElements()).isEqualTo(15));
        }

        @Test
        @DisplayName("템플릿 검색 성공: 카테고리 없음")
        void findAllBySuccessNoCategory() {
            Member member = MemberFixture.getFirstMember();
            Category category1 = CategoryFixture.getFirstCategory();
            Category category2 = CategoryFixture.getSecondCategory();
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            FindAllTemplatesResponse allBy = templateApplicationService.findAllBy(
                    member.getId(), "hello keyword", null, null, PAGEABLE
            );

            assertAll(
                    () -> assertThat(allBy.templates()).hasSize(3),
                    () -> assertThat(allBy.templates()).extracting("id").containsExactly(2L, 3L, 4L),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("템플릿 검색 실패: 카테고리 없음")
        void findAllByFailNotCategory() {
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getFirstCategory();
            saveDefault15Templates(member, category);
            Long memberId = member.getId();

            assertThatThrownBy(() -> templateApplicationService.findAllBy(
                    memberId, "", 3L, null, PAGEABLE
            ))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 3에 해당하는 카테고리가 존재하지 않습니다.");
        }

        private void saveDefault15Templates(Member member, Category category) {
            saveTemplate(makeTemplateRequest("hello keyword 1"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 2"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 3"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 4"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 5"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 6"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 7"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 8"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 9"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 10"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 11"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 12"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 13"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 14"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword 15"), member, category);
        }
    }

    @Nested
    @DisplayName("템플릿 수정")
    class update {
        @Test
        @DisplayName("템플릿 수정 성공")
        void updateSuccess() {
            // given
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getFirstCategory();
            CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
            Template template = saveTemplate(createdTemplate, member, category);
            UpdateTemplateRequest updateTemplateRequest = makeUpdateTemplateRequest(category.getId());

            // when
            templateApplicationService.update(member, template.getId(), updateTemplateRequest);
            Template updateTemplate = templateRepository.fetchById(template.getId());
            List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);
            Thumbnail thumbnail = thumbnailRepository.fetchByTemplateId(template.getId());
            List<Tag> tags = templateTagRepository.findAllByTemplate(updateTemplate).stream()
                    .map(TemplateTag::getTag)
                    .toList();

            // then
            assertAll(
                    () -> assertThat(updateTemplate.getTitle()).isEqualTo("updateTitle"),
                    () -> assertThat(thumbnail.getSourceCode().getId()).isEqualTo(2L),
                    () -> assertThat(sourceCodes).hasSize(3),
                    () -> assertThat(updateTemplate.getCategory().getId()).isEqualTo(1L),
                    () -> assertThat(tags).hasSize(2),
                    () -> assertThat(tags.get(1).getName()).isEqualTo("tag3")
            );
        }

        @Test
        @DisplayName("템플릿 수정 실패 : 카테고리 없음")
        void createTemplateFailNotCategory() {
            // given
            Member member = MemberFixture.getFirstMember();
            CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
            Long templateId = saveTemplate(createdTemplate, member, CategoryFixture.getFirstCategory()).getId();
            UpdateTemplateRequest updateTemplateRequest = makeUpdateTemplateRequest(3L);

            // when & then
            assertThatThrownBy(() -> templateApplicationService.update(member, templateId, updateTemplateRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 3에 해당하는 카테고리가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("템플릿 수정 실패 : 카테고리 권한 없음")
        void createTemplateFail() {
            // given
            Member member = MemberFixture.getFirstMember();
            CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
            Long templateId = saveTemplate(createdTemplate, member, CategoryFixture.getFirstCategory()).getId();
            UpdateTemplateRequest updateTemplateRequest =
                    makeUpdateTemplateRequest(CategoryFixture.getSecondCategory().getId());

            // when & then
            assertThatThrownBy(() -> templateApplicationService.update(member, templateId, updateTemplateRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 카테고리에 대한 권한이 없습니다.");
        }

        private UpdateTemplateRequest makeUpdateTemplateRequest(Long categoryId) {
            return new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    List.of(
                            new CreateSourceCodeRequest("filename3", "content3", 2),
                            new CreateSourceCodeRequest("filename4", "content4", 3)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "filename2", "content2", 1)
                    ),
                    List.of(1L),
                    categoryId,
                    List.of("tag1", "tag3")
            );
        }
    }

    private Template saveTemplate(CreateTemplateRequest createTemplateRequest, Member member, Category category) {
        Template savedTemplate = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category));
        SourceCode savedFirstSourceCode = sourceCodeRepository.save(
                new SourceCode(savedTemplate, "filename1", "content1", 1));
        sourceCodeRepository.save(new SourceCode(savedTemplate, "filename2", "content2", 2));
        savedTemplate.updateSourceCodes(List.of(savedFirstSourceCode));
        thumbnailRepository.save(new Thumbnail(savedTemplate, savedFirstSourceCode));

        return savedTemplate;
    }

    private CreateTemplateRequest makeTemplateRequest(String title) {
        return new CreateTemplateRequest(
                title,
                "description",
                List.of(new CreateSourceCodeRequest("filename1", "content1", 1),
                        new CreateSourceCodeRequest("filename2", "content2", 2)),
                1,
                1L,
                List.of()
        );
    }
}
