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
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.category.service.CategoryService;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberDtoFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.member.service.MemberService;
import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindTagResponse;
import codezap.tag.repository.TagRepository;
import codezap.tag.repository.TemplateTagRepository;
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
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.fixture.SourceCodeFixture;
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

class MemberTemplateApplicationServiceTest {
    private final TemplateRepository templateRepository = new FakeTemplateRepository(
            List.of(TemplateFixture.getFirst(), TemplateFixture.getSecond(), TemplateFixture.getThird())
    );
    private final ThumbnailRepository thumbnailRepository = new FakeThumbnailRepository();
    private final SourceCodeRepository sourceCodeRepository = new FakeSourceCodeRepository(
            List.of(SourceCodeFixture.getFirst(), SourceCodeFixture.getSecond(), SourceCodeFixture.getThird())
    );
    public final TemplateTagRepository templateTagRepository = new FakeTemplateTagRepository();
    public final TagRepository tagRepository =
            new FakeTagRepository(List.of(TagFixture.getFirst(), TagFixture.getSecond()));
    public final CategoryRepository categoryRepository = new FakeCategoryRepository(
            List.of(CategoryFixture.getFirstCategory(), CategoryFixture.getSecondCategory())
    );
    public final MemberRepository memberRepository = new FakeMemberRepository(
            List.of(MemberFixture.getFirstMember(), MemberFixture.getSecondMember())
    );

    private final TemplateService templateService = new TemplateService(templateRepository);

    private final TemplateTagService templateTagService = new TemplateTagService(tagRepository, templateTagRepository);

    private final ThumbnailService thumbnailService = new ThumbnailService(thumbnailRepository);

    private final SourceCodeService sourceCodeService = new SourceCodeService(sourceCodeRepository);

    private final CategoryService categoryService = new CategoryService(categoryRepository);

    private final MemberService memberService = new MemberService(memberRepository, categoryRepository);

    private final TagTemplateApplicationService tagTemplateApplicationService =
            new TagTemplateApplicationService(templateTagService, templateService, thumbnailService, sourceCodeService);

    private final TemplateApplicationService templateApplicationService =
            new TemplateApplicationService(categoryService, tagTemplateApplicationService);

    private final MemberTemplateApplicationService memberTemplateApplicationService =
            new MemberTemplateApplicationService(
                    memberService, templateApplicationService, tagTemplateApplicationService
            );

    @Nested
    @DisplayName("템플릿 생성")
    class createTemplate {
        @Test
        @DisplayName("템플릿 생성 성공")
        void createTemplateSuccess() {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
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
            Long id = memberTemplateApplicationService.createTemplate(memberDto, createTemplateRequest);
            Template template = templateRepository.fetchById(id);

            // then
            assertAll(
                    () -> assertThat(templateRepository.findAll()).hasSize(4),
                    () -> assertThat(template.getTitle()).isEqualTo(createTemplateRequest.title()),
                    () -> assertThat(template.getCategory().getName()).isEqualTo(category.getName())
            );
        }
    }

    @Nested
    @DisplayName("태그 목록 조회")
    class getAllTagsByMemberId {
        @Test
        @DisplayName("태그 목록 조회 성공")
        void getAllTagsByMemberIdSuccess() {
            // given
            Member member1 = MemberFixture.getFirstMember();
            MemberDto memberDto1 = MemberDtoFixture.getFirstMemberDto();
            Member member2 = MemberFixture.getSecondMember();
            MemberDto memberDto2 = MemberDtoFixture.getSecondMemberDto();
            templateTagRepository.save(new TemplateTag(TemplateFixture.getFirst(), TagFixture.getFirst()));
            templateTagRepository.save(new TemplateTag(TemplateFixture.getFirst(), TagFixture.getSecond()));
            templateTagRepository.save(new TemplateTag(TemplateFixture.getThird(), TagFixture.getFirst()));

            // when
            List<FindTagResponse> tags1 =
                    memberTemplateApplicationService.getAllTagsByMemberId(memberDto1, member1.getId()).tags();
            List<FindTagResponse> tags2 =
                    memberTemplateApplicationService.getAllTagsByMemberId(memberDto2, member2.getId()).tags();

            // then
            assertAll(
                    () -> assertThat(tags1).hasSize(2),
                    () -> assertThat(tags2).hasSize(1)
            );
        }
    }

    @Nested
    @DisplayName("단건 조회")
    class getByIdAndMember {
        @Test
        @DisplayName("템플릿 단건 조회 성공")
        void getByIdAndMemberSuccess() {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Template template = TemplateFixture.getFirst();
            templateTagRepository.save(new TemplateTag(template, TagFixture.getFirst()));

            // when
            FindTemplateResponse foundTemplate =
                    memberTemplateApplicationService.getByIdAndMember(memberDto, template.getId());

            // then
            assertAll(
                    () -> assertThat(foundTemplate.title()).isEqualTo(template.getTitle()),
                    () -> assertThat(foundTemplate.sourceCodes()).hasSize(
                            sourceCodeRepository.findAllByTemplate(template).size()),
                    () -> assertThat(foundTemplate.category().id()).isEqualTo(template.getCategory().getId()),
                    () -> assertThat(foundTemplate.tags()).hasSize(1)
            );
        }
    }

    @Nested
    @DisplayName("템플릿 검색")
    class findAllBy {
        public static final PageRequest PAGEABLE = PageRequest.of(1, 3);

        @Test
        @DisplayName("템플릿 검색 성공: 카테고리 O")
        void findAllBySuccess() {
            Member member = MemberFixture.getFirstMember();
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Category category1 = CategoryFixture.getFirstCategory();
            Category category2 = CategoryFixture.getSecondCategory();
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            FindAllTemplatesResponse allBy = memberTemplateApplicationService.findAllBy(
                    memberDto, member.getId(), "hello keyword", category2.getId(), null, PAGEABLE
            );

            assertAll(
                    () -> assertThat(allBy.templates()).hasSize(3),
                    () -> assertThat(allBy.templates()).extracting("id").containsExactly(19L, 20L, 21L),
                    () -> assertThat(allBy.totalElements()).isEqualTo(15));
        }

        @Test
        @DisplayName("템플릿 검색 성공: 카테고리 X")
        void findAllBySuccessNoCategory() {
            Member member = MemberFixture.getFirstMember();
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Category category1 = CategoryFixture.getFirstCategory();
            Category category2 = CategoryFixture.getSecondCategory();
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            FindAllTemplatesResponse allBy = memberTemplateApplicationService.findAllBy(
                    memberDto, member.getId(), "hello keyword", null, null, PAGEABLE
            );

            assertAll(
                    () -> assertThat(allBy.templates()).hasSize(3),
                    () -> assertThat(allBy.templates()).extracting("id").containsExactly(4L, 5L, 6L),
                    () -> assertThat(allBy.totalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("템플릿 검색 실패: 인증 정보 다름")
        void findAllByFailNotUnAuth() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();

            assertThatThrownBy(() -> memberTemplateApplicationService.findAllBy(
                    memberDto, 2L, "", 3L, null, PAGEABLE
            ))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("인증 정보에 포함된 멤버 ID와 파라미터로 받은 멤버 ID가 다릅니다.");
        }

        @Test
        @DisplayName("템플릿 검색 실패: 인증 정보 다름")
        void findAllByFailNotMember() {
            MemberDto memberDto = new MemberDto(3L, "member3", "1234");

            assertThatThrownBy(() -> memberTemplateApplicationService.findAllBy(
                    memberDto, 3L, "", 3L, null, PAGEABLE
            ))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("로그인 정보가 잘못되었습니다.");
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
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Category category = CategoryFixture.getFirstCategory();
            CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
            Template template = saveTemplate(createdTemplate, member, category);
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    List.of(
                            new CreateSourceCodeRequest("filename3", "content3", 3),
                            new CreateSourceCodeRequest("filename4", "content4", 4)
                    ),
                    List.of(
                            new UpdateSourceCodeRequest(2L, "filename2", "content2", 1),
                            new UpdateSourceCodeRequest(3L, "filename2", "content2", 2)
                    ),
                    List.of(1L),
                    category.getId(),
                    List.of("tag1", "tag3")
            );

            // when
            memberTemplateApplicationService.update(memberDto, template.getId(), updateTemplateRequest);
            Template updateTemplate = templateRepository.fetchById(template.getId());
            List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);
            Thumbnail thumbnail = thumbnailRepository.fetchByTemplateId(template.getId());
            List<Tag> tags = templateTagRepository.findAllByTemplate(updateTemplate).stream()
                    .map(TemplateTag::getTag)
                    .toList();

            // then
            assertAll(
                    () -> assertThat(updateTemplate.getTitle()).isEqualTo("updateTitle"),
                    () -> assertThat(thumbnail.getSourceCode().getId()).isEqualTo(4L),
                    () -> assertThat(sourceCodes).hasSize(4),
                    () -> assertThat(updateTemplate.getCategory().getId()).isEqualTo(1L),
                    () -> assertThat(tags).hasSize(2),
                    () -> assertThat(tags.get(1).getName()).isEqualTo("tag3")
            );
        }

        @Test
        @DisplayName("템플릿 수정 실패: 멤버 아이디가 없음")
        void updateFailNotMemberId() {
            // given
            Member member = MemberFixture.getFirstMember();
            MemberDto memberDto = new MemberDto(3L, "name", "1234");
            Category category = CategoryFixture.getFirstCategory();
            CreateTemplateRequest createdTemplate = makeTemplateRequest("title");
            Long templateId = saveTemplate(createdTemplate, member, category).getId();
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest(
                    "updateTitle",
                    "description",
                    List.of(),
                    List.of(),
                    List.of(1L),
                    category.getId(),
                    List.of("tag1", "tag3")
            );

            // when
            assertThatThrownBy(() ->
                    memberTemplateApplicationService.update(memberDto, templateId, updateTemplateRequest)
            )
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("식별자 3에 해당하는 멤버가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 삭제")
    class deleteByMemberAndIds {
        @Test
        @DisplayName("템플릿 삭제 성공: 1개")
        void deleteByMemberAndIdsSuccess() {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Template template = TemplateFixture.getFirst();

            // when
            memberTemplateApplicationService.deleteByIds(memberDto, List.of(template.getId()));

            // then
            assertAll(
                    () -> assertThat(templateRepository.findAll()).hasSize(2),
                    () -> assertThat(sourceCodeRepository.findAll()).hasSize(1),
                    () -> assertThat(thumbnailRepository.findAll()).isEmpty()
            );
        }

        @Test
        @DisplayName("템플릿 삭제 성공: 2개")
        void deleteByMemberAndIdsSuccessMany() {
            // given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Template template1 = TemplateFixture.getFirst();
            Template template2 = TemplateFixture.getSecond();

            // when
            memberTemplateApplicationService.deleteByIds(memberDto, List.of(template1.getId(), template2.getId()));

            // then
            assertThat(templateRepository.findAll()).hasSize(1);
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
