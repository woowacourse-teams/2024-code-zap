package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberDtoFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.tag.domain.Tag;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.FakeSourceCodeRepository;
import codezap.template.repository.FakeTagRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.FakeTemplateTagRepository;
import codezap.template.repository.FakeThumbnailRepository;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailRepository;

class TemplateServiceTest {

    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final SourceCodeRepository sourceCodeRepository = new FakeSourceCodeRepository();
    private final ThumbnailRepository thumbnailRepository = new FakeThumbnailRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository(
            List.of(CategoryFixture.getFirstCategory(), CategoryFixture.getSecondCategory())
    );
    private final TemplateTagRepository templateTagRepository = new FakeTemplateTagRepository();

    private final TagRepository tagRepository = new FakeTagRepository();

    private final MemberRepository memberRepository = new FakeMemberRepository(
            List.of(MemberFixture.getFirstMember(), MemberFixture.getSecondMember())
    );
    private final TemplateService templateService = new TemplateService(templateRepository);

    private CreateTemplateRequest makeTemplateRequest(String title) {
        return new CreateTemplateRequest(
                title,
                "description",
                List.of(
                        new CreateSourceCodeRequest("filename1", "content1", 1),
                        new CreateSourceCodeRequest("filename2", "content2", 2)
                ),
                1,
                1L,
                List.of("tag1", "tag2")
        );
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

    private Template saveTemplate(CreateTemplateRequest createTemplateRequest, Category category, Member member) {
        Category savedCategory = categoryRepository.save(category);
        Template savedTemplate = templateRepository.save(
                new Template(
                        member,
                        createTemplateRequest.title(),
                        createTemplateRequest.description(),
                        savedCategory
                )
        );
        SourceCode savedFirstSourceCode = sourceCodeRepository.save(
                new SourceCode(savedTemplate, "filename1", "content1", 1));
        sourceCodeRepository.save(new SourceCode(savedTemplate, "filename2", "content2", 2));
        thumbnailRepository.save(new Thumbnail(savedTemplate, savedFirstSourceCode));
        createTemplateRequest.tags().stream()
                .map(Tag::new)
                .map(tagRepository::save)
                .forEach(tag -> templateTagRepository.save(new TemplateTag(savedTemplate, tag)));

        return savedTemplate;
    }

    @Test
    @DisplayName("템플릿 전체 조회 성공")
    void findAllTemplatesSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        saveTemplate(makeTemplateRequest("title1"), new Category("category1", member), member);
        saveTemplate(makeTemplateRequest("title2"), new Category("category2", member), member);

        // when
        Page<Template> allTemplates = templateService.findAllBy(
                member.getId(), "", PageRequest.of(0, 10));

        // then
        assertThat(allTemplates).hasSize(2);
    }

    @Test
    @DisplayName("템플릿 단건 조회 성공")
    void findOneTemplateSuccess() {
        // given
        Member member = MemberFixture.getFirstMember();
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        Template foundTemplate = templateService.getByMemberAndId(member, template.getId());

        // then
        assertAll(
                () -> assertThat(foundTemplate.getTitle()).isEqualTo(template.getTitle()),
                () -> assertThat(foundTemplate.getCategory().getId()).isEqualTo(template.getCategory().getId())
        );
    }

    @Test
    @DisplayName("템플릿 단건 조회 실패: 권한 없음")
    void findOneTemplateFailWithUnauthorized() {
        // given
        Member member = MemberFixture.getFirstMember();
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        Member otherMember = MemberFixture.getSecondMember();

        // then
        Long templateId = template.getId();
        assertThatThrownBy(() -> templateService.getByMemberAndId(otherMember, templateId))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("템플릿 생성 성공")
    void createTemplateSuccess() {
        // given
        Member member = MemberFixture.getFirstMember();
        Category category = CategoryFixture.getFirstCategory();
        CreateTemplateRequest createTemplateRequest = makeTemplateRequest("title");

        // when
        Template template = templateService.createTemplate(member, category, createTemplateRequest);

        // then
        assertAll(
                () -> assertThat(templateRepository.findAll()).hasSize(1),
                () -> assertThat(template.getTitle()).isEqualTo(createTemplateRequest.title()),
                () -> assertThat(template.getCategory().getName()).isEqualTo("카테고리 없음")
        );
    }

    @Test
    @DisplayName("템플릿 수정 성공")
    void updateTemplateSuccess() {
        // given
        Member member = MemberFixture.getFirstMember();
        Category category = CategoryFixture.getFirstCategory();
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, category, member);
        categoryRepository.save(category);

        // when
        UpdateTemplateRequest updateTemplateRequest = makeUpdateTemplateRequest(1L);
        templateService.updateTemplate(member, category, template.getId(), updateTemplateRequest);
        Template updateTemplate = templateRepository.fetchById(template.getId());
        List<Tag> tags = templateTagRepository.findAllByTemplate(updateTemplate).stream()
                .map(TemplateTag::getTag)
                .toList();

        // then
        assertAll(
                () -> assertThat(updateTemplate.getTitle()).isEqualTo("updateTitle"),
                () -> assertThat(updateTemplate.getCategory().getId()).isEqualTo(1L),
                () -> assertThat(tags).hasSize(2)
        );
    }

    @Test
    @DisplayName("템플릿 수정 실패: 권한 없음")
    void updateTemplateFailWithUnauthorized() {
        // given
        Member member = MemberFixture.getFirstMember();
        Category category = CategoryFixture.getFirstCategory();
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);
        categoryRepository.save(new Category("category2", member));

        // when
        Member otherMember = MemberFixture.getSecondMember();
        UpdateTemplateRequest updateTemplateRequest = makeUpdateTemplateRequest(2L);

        // then
        Long templateId = template.getId();
        assertThatThrownBy(
                () -> templateService.updateTemplate(otherMember, category, templateId, updateTemplateRequest))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("템플릿 삭제 성공: 1개")
    void deleteTemplateSuccess() {
        // given
        Member member = MemberFixture.getFirstMember();
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        templateService.deleteByMemberAndIds(member, List.of(1L));

        // then
        assertThat(templateRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("템플릿 삭제 성공: 2개")
    void deleteTemplatesSuccess() {
        // given
        Member member = MemberFixture.getFirstMember();
        CreateTemplateRequest createdTemplate1 = makeTemplateRequest("title1");
        CreateTemplateRequest createdTemplate2 = makeTemplateRequest("title2");
        saveTemplate(createdTemplate1, new Category("category1", member), member);
        saveTemplate(createdTemplate2, new Category("category1", member), member);

        // when
        templateService.deleteByMemberAndIds(member, List.of(1L, 2L));

        // then
        assertThat(templateRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("템플릿 삭제 실패: 권한 없음")
    void deleteTemplateFailWithUnauthorized() {
        // given
        Member member = MemberFixture.getFirstMember();
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        Member otherMember = MemberFixture.getSecondMember();

        // then
        List<Long> ids = List.of(1L);
        assertThatThrownBy(() -> templateService.deleteByMemberAndIds(otherMember, ids))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("템플릿 삭제 실패: 동일한 ID 2개가 들어왔을 때")
    void deleteTemplatesFailWithSameTemplateId() {
        // given
        Member member = MemberFixture.getFirstMember();
        CreateTemplateRequest createdTemplate1 = makeTemplateRequest("title1");
        CreateTemplateRequest createdTemplate2 = makeTemplateRequest("title2");
        saveTemplate(createdTemplate1, new Category("category1", member), member);
        saveTemplate(createdTemplate2, new Category("category1", member), member);

        // when & then
        List<Long> ids = List.of(1L, 1L);
        assertThatThrownBy(() -> templateService.deleteByMemberAndIds(member, ids))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("삭제하고자 하는 템플릿 ID가 중복되었습니다.");
    }
}
