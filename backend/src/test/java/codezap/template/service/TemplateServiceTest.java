package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.fixture.MemberDtoFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.Snippet;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.ThumbnailSnippet;
import codezap.template.dto.request.CreateSnippetRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSnippetRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.FindAllTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.repository.FakeSnippetRepository;
import codezap.template.repository.FakeTagRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.FakeTemplateTagRepository;
import codezap.template.repository.FakeThumbnailSnippetRepository;
import codezap.template.repository.SnippetRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailSnippetRepository;

class TemplateServiceTest {

    private final Member firstMember = new Member(1L, "test1@email.com", "password1234", "username1");
    private final Member secondMember = new Member(2L, "test2@email.com", "password1234", "username2");
    private final Category firstCategory = new Category(1L, firstMember, "카테고리 없음", true);
    private final Category secondCategory = new Category(2L, secondMember, "카테고리 없음", true);

    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final SnippetRepository snippetRepository = new FakeSnippetRepository();
    private final ThumbnailSnippetRepository thumbnailSnippetRepository = new FakeThumbnailSnippetRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository(
            List.of(firstCategory, secondCategory));
    private final TemplateTagRepository templateTagRepository = new FakeTemplateTagRepository();
    private final TagRepository tagRepository = new FakeTagRepository();
    private final MemberRepository memberRepository = new FakeMemberRepository(List.of(firstMember, secondMember));
    private final TemplateService templateService = new TemplateService(
            thumbnailSnippetRepository,
            templateRepository,
            snippetRepository,
            categoryRepository,
            tagRepository,
            templateTagRepository,
            memberRepository);

    @Test
    @DisplayName("템플릿 생성 성공")
    void createTemplateSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        CreateTemplateRequest createTemplateRequest = makeTemplateRequest("title");

        // when
        Long id = templateService.createTemplate(createTemplateRequest, memberDto);
        Template template = templateRepository.fetchById(id);

        // then
        assertAll(
                () -> assertThat(templateRepository.findAll()).hasSize(1),
                () -> assertThat(template.getTitle()).isEqualTo(createTemplateRequest.title()),
                () -> assertThat(template.getCategory().getName()).isEqualTo("카테고리 없음")
        );
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
        FindAllTemplatesResponse allTemplates = templateService.findAllBy(
                member.getId(), "", null, null, PageRequest.of(1, 10));

        // then
        assertThat(allTemplates.templates()).hasSize(2);
    }

    @Test
    @DisplayName("템플릿 단건 조회 성공")
    void findOneTemplateSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        FindTemplateResponse foundTemplate = templateService.findByIdAndMember(template.getId(), memberDto);

        // then
        assertAll(
                () -> assertThat(foundTemplate.title()).isEqualTo(template.getTitle()),
                () -> assertThat(foundTemplate.snippets()).hasSize(
                        snippetRepository.findAllByTemplate(template).size()),
                () -> assertThat(foundTemplate.category().id()).isEqualTo(template.getCategory().getId()),
                () -> assertThat(foundTemplate.tags()).hasSize(2)
        );
    }

    @Test
    @DisplayName("템플릿 단건 조회 실패: 권한 없음")
    void findOneTemplateFailWithUnauthorized() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        MemberDto otherMemberDto = MemberDtoFixture.getSecondMemberDto();

        // then
        Long templateId = template.getId();
        assertThatThrownBy(() -> templateService.findByIdAndMember(templateId, otherMemberDto))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("템플릿 수정 성공")
    void updateTemplateSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);
        categoryRepository.save(new Category("category2", member));

        // when
        UpdateTemplateRequest updateTemplateRequest = makeUpdateTemplateRequest(1L);
        templateService.update(memberDto, template.getId(), updateTemplateRequest);
        Template updateTemplate = templateRepository.fetchById(template.getId());
        List<Snippet> snippets = snippetRepository.findAllByTemplate(template);
        ThumbnailSnippet thumbnailSnippet = thumbnailSnippetRepository.fetchById(template.getId());
        List<Tag> tags = templateTagRepository.findAllByTemplate(updateTemplate).stream()
                .map(TemplateTag::getTag)
                .toList();

        // then
        assertAll(
                () -> assertThat(updateTemplate.getTitle()).isEqualTo("updateTitle"),
                () -> assertThat(thumbnailSnippet.getSnippet().getId()).isEqualTo(2L),
                () -> assertThat(snippets).hasSize(3),
                () -> assertThat(updateTemplate.getCategory().getId()).isEqualTo(1L),
                () -> assertThat(tags).hasSize(2),
                () -> assertThat(tags.get(1).getName()).isEqualTo("tag3")
        );
    }

    @Test
    @DisplayName("템플릿 수정 실패: 권한 없음")
    void updateTemplateFailWithUnauthorized() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        Template template = saveTemplate(createdTemplate, new Category("category1", member), member);
        categoryRepository.save(new Category("category2", member));

        // when
        MemberDto otherMemberDto = MemberDtoFixture.getSecondMemberDto();
        UpdateTemplateRequest updateTemplateRequest = makeUpdateTemplateRequest(2L);

        // then
        Long templateId = template.getId();
        assertThatThrownBy(() -> templateService.update(otherMemberDto, templateId, updateTemplateRequest))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("템플릿 삭제 성공: 1개")
    void deleteTemplateSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        templateService.deleteByIds(memberDto, List.of(1L));

        // then
        assertAll(
                () -> assertThat(templateRepository.findAll()).isEmpty(),
                () -> assertThat(snippetRepository.findAll()).isEmpty(),
                () -> assertThat(thumbnailSnippetRepository.findAll()).isEmpty()
        );
    }

    @Test
    @DisplayName("템플릿 삭제 성공: 2개")
    void deleteTemplatesSuccess() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate1 = makeTemplateRequest("title1");
        CreateTemplateRequest createdTemplate2 = makeTemplateRequest("title2");
        saveTemplate(createdTemplate1, new Category("category1", member), member);
        saveTemplate(createdTemplate2, new Category("category1", member), member);

        // when
        templateService.deleteByIds(memberDto, List.of(1L, 2L));

        // then
        assertAll(
                () -> assertThat(templateRepository.findAll()).isEmpty(),
                () -> assertThat(snippetRepository.findAll()).isEmpty(),
                () -> assertThat(thumbnailSnippetRepository.findAll()).isEmpty()
        );
    }

    @Test
    @DisplayName("템플릿 삭제 실패: 권한 없음")
    void deleteTemplateFailWithUnauthorized() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
        saveTemplate(createdTemplate, new Category("category1", member), member);

        // when
        MemberDto otherMemberDto = MemberDtoFixture.getSecondMemberDto();

        // then
        List<Long> ids = List.of(1L);
        assertThatThrownBy(() -> templateService.deleteByIds(otherMemberDto, ids))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("템플릿 삭제 실패: 동일한 ID 2개가 들어왔을 때")
    void deleteTemplatesFailWithSameTemplateId() {
        // given
        MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        Member member = memberRepository.fetchById(memberDto.id());
        CreateTemplateRequest createdTemplate1 = makeTemplateRequest("title1");
        CreateTemplateRequest createdTemplate2 = makeTemplateRequest("title2");
        saveTemplate(createdTemplate1, new Category("category1", member), member);
        saveTemplate(createdTemplate2, new Category("category1", member), member);

        // when & then
        List<Long> ids = List.of(1L, 1L);
        assertThatThrownBy(() -> templateService.deleteByIds(memberDto, ids))
                .isInstanceOf(CodeZapException.class)
                .hasMessage("식별자 1에 해당하는 템플릿이 존재하지 않습니다.");
    }

    private CreateTemplateRequest makeTemplateRequest(String title) {
        return new CreateTemplateRequest(
                title,
                "description",
                List.of(
                        new CreateSnippetRequest("filename1", "content1", 1),
                        new CreateSnippetRequest("filename2", "content2", 2)
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
                        new CreateSnippetRequest("filename3", "content3", 2),
                        new CreateSnippetRequest("filename4", "content4", 3)
                ),
                List.of(
                        new UpdateSnippetRequest(2L, "filename2", "content2", 1)
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
        Snippet savedFirstSnippet = snippetRepository.save(new Snippet(savedTemplate, "filename1", "content1", 1));
        snippetRepository.save(new Snippet(savedTemplate, "filename2", "content2", 2));
        thumbnailSnippetRepository.save(new ThumbnailSnippet(savedTemplate, savedFirstSnippet));
        createTemplateRequest.tags().stream()
                .map(Tag::new)
                .map(tagRepository::save)
                .forEach(tag -> templateTagRepository.save(new TemplateTag(savedTemplate, tag)));

        return savedTemplate;
    }
}