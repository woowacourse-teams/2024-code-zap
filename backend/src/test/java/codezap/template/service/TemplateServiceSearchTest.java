package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.category.repository.FakeCategoryRepository;
import codezap.fixture.MemberDtoFixture;
import codezap.fixture.MemberFixture;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.repository.FakeMemberRepository;
import codezap.member.repository.MemberRepository;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.repository.FakeSourceCodeRepository;
import codezap.template.repository.FakeTagRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.FakeTemplateTagRepository;
import codezap.template.repository.FakeThumbnailRepository;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TagRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateTagRepository;
import codezap.template.repository.ThumbnailRepository;

class TemplateServiceSearchTest {
    private final TemplateRepository templateRepository = new FakeTemplateRepository();
    private final SourceCodeRepository sourceCodeRepository = new FakeSourceCodeRepository();
    private final ThumbnailRepository thumbnailRepository = new FakeThumbnailRepository();
    private final CategoryRepository categoryRepository = new FakeCategoryRepository();
    private final TemplateTagRepository templateTagRepository = new FakeTemplateTagRepository();
    private final TagRepository tagRepository = new FakeTagRepository();
    private final MemberRepository memberRepository = new FakeMemberRepository(List.of(
            MemberFixture.getFirstMember(), MemberFixture.getSecondMember()
    ));
    private final TemplateService templateService = new TemplateService(
            thumbnailRepository,
            templateRepository,
            sourceCodeRepository
    );

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

    private CreateTemplateRequest makeTemplateRequest(String title) {
        return new CreateTemplateRequest(title, "description",
                List.of(new CreateSourceCodeRequest("filename1", "content1", 1),
                        new CreateSourceCodeRequest("filename2", "content2", 2)),
                1,
                1L,
                List.of());
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

    private void saveTemplateByFilename(String templateTitle, String firstFilename, String secondFilename,
            Member member, Category category
    ) {
        Template savedTemplate = templateRepository.save(new Template(member, templateTitle, "설명", category));
        SourceCode savedFirstSourceCode = sourceCodeRepository.save(
                new SourceCode(savedTemplate, firstFilename, "content1", 1));
        SourceCode savedSecondSourceCode = sourceCodeRepository.save(
                new SourceCode(savedTemplate, secondFilename, "content2", 2));
        savedTemplate.updateSourceCodes(List.of(savedFirstSourceCode, savedSecondSourceCode));
        thumbnailRepository.save(new Thumbnail(savedTemplate, savedFirstSourceCode));
    }

    private void saveTemplateBySourceCode(String templateTitle, String firstContent, String secondContent,
            Member member, Category category
    ) {
        Template savedTemplate = templateRepository.save(new Template(member, templateTitle, "설명", category));
        SourceCode savedFirstSourceCode = sourceCodeRepository.save(
                new SourceCode(savedTemplate, "filename1", firstContent, 1));
        SourceCode savedSecondSourceCode = sourceCodeRepository.save(
                new SourceCode(savedTemplate, "filename2", secondContent, 2));
        savedTemplate.updateSourceCodes(List.of(savedFirstSourceCode, savedSecondSourceCode));
        thumbnailRepository.save(new Thumbnail(savedTemplate, savedFirstSourceCode));
    }

    @Nested
    @DisplayName("템플릿 토픽 검색")
    class searchContainKeyword {
        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 템플릿 제목에 포함")
        void findAllTemplatesTitleContainKeywordSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            saveTemplate(makeTemplateRequest("hello"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword"), member, category);
            saveTemplate(makeTemplateRequest("keyword hello"), member, category);
            saveTemplate(makeTemplateRequest("hello keyword !"), member, category);

            //when
            Page<Template> templates = templateService.findAllBy(
                    member.getId(), "keyword", PageRequest.of(0, 3)
            );

            //then
            assertThat(templates).hasSize(3);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 내에 파일명 중 하나라도 포함")
        void findAllFilenameContainKeywordSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            saveTemplateByFilename("tempate1", "login.js", "signup.js", member, category);
            saveTemplateByFilename("tempate2", "login.java", "signup.java", member, category);
            saveTemplateByFilename("tempate3", "login.js", "signup.java", member, category);

            //when
            Page<Template> templates = templateService.findAllBy(
                    member.getId(), "java", PageRequest.of(0, 3)
            );

            //then
            assertThat(templates).hasSize(2);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 내에 소스 코드 중 하나라도 포함")
        void findAllSourceCodeContainKeywordSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            saveTemplateBySourceCode("tempate1", "public Main {", "new Car();", member, category);
            saveTemplateBySourceCode("tempate2", "private Car", "public Movement", member, category);
            saveTemplateBySourceCode("tempate3", "console.log", "a+b=3", member, category);

            //when
            Page<Template> templates = templateService.findAllBy(
                    member.getId(), "Car", PageRequest.of(0, 3)
            );

            //then
            assertThat(templates).hasSize(2);
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 탬플릿 설명에 포함")
        void findAllDescriptionContainKeywordSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            CreateTemplateRequest request1 = new CreateTemplateRequest("타이틀", "Login 구현",
                    List.of(new CreateSourceCodeRequest("filename1", "content1", 1),
                            new CreateSourceCodeRequest("filename2", "content2", 2)),
                    1,
                    category.getId(),
                    List.of("tag1", "tag2"));
            saveTemplate(request1, member, category);
            CreateTemplateRequest request2 = new CreateTemplateRequest("타이틀", "Signup 구현",
                    List.of(new CreateSourceCodeRequest("filename1", "content1", 1),
                            new CreateSourceCodeRequest("filename2", "content2", 2)),
                    1,
                    category.getId(),
                    List.of("tag1", "tag2"));
            saveTemplate(request2, member, category);

            //when
            Page<Template> templates = templateService.findAllBy(
                    member.getId(), "Login", PageRequest.of(0, 3)
            );

            //then
            assertThat(templates).hasSize(1);
        }

        @Test
        @DisplayName("전체 탐색 / 1페이지 성공")
        void findAllFirstPageSuccess() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);
            Page<Template> allBy = templateService.findAllBy(
                    member.getId(), "", PageRequest.of(0, 20)
            );

            assertAll(() -> assertThat(allBy).hasSize(20),
                    () -> assertThat(allBy).allMatch(template -> template.getId() <= 20),
                    () -> assertThat(allBy.getTotalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("템플릿 토픽 검색 성공 : 페이징 성공")
        void findAllContainKeywordPaging() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category = categoryRepository.save(new Category("category", member));
            saveDefault15Templates(member, category);

            //when
            Page<Template> templates = templateService.findAllBy(
                    member.getId(), "keyword", PageRequest.of(1, 5)
            );

            //then
            List<String> titles = templates.stream().map(Template::getTitle).toList();
            assertThat(titles).containsExactly("hello keyword 6", "hello keyword 7", "hello keyword 8",
                    "hello keyword 9",
                    "hello keyword 10");
        }
    }

    @Nested
    @DisplayName("조건에 따른 페이지 조회 메서드 동작 확인")
    class FilteringPageTest {

        private static final PageRequest DEFAULT_PAGING_REQUEST = PageRequest.of(0, 20);

        @Test
        @DisplayName("전체 탐색 / 1페이지 성공")
        void findAllFirstPageSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            Page<Template> allBy = templateService.findAllBy(
                    member.getId(), "", PageRequest.of(0, 20)
            );

            //when & then
            assertAll(
                    () -> assertThat(allBy).hasSize(20),
                    () -> assertThat(allBy).allMatch(template -> template.getId() <= 20),
                    () -> assertThat(allBy.getTotalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("전체 탐색 / 2페이지 성공")
        void findAllSecondPageSuccess() {
            //given
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            Page<Template> allBy = templateService.findAllBy(
                    member.getId(), "", PageRequest.of(1, 20)
            );

            assertAll(() -> assertThat(allBy).hasSize(10),
                    () -> assertThat(allBy).allMatch(template -> template.getId() > 20),
                    () -> assertThat(allBy.getTotalElements()).isEqualTo(30));
        }

        @Test
        @DisplayName("카테고리 탐색 성공")
        void findByCategoryPageSuccess() {
            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
            Member member = memberRepository.fetchById(memberDto.id());
            Category category1 = categoryRepository.save(new Category("category1", member));
            Category category2 = categoryRepository.save(new Category("category2", member));
            saveDefault15Templates(member, category1);
            saveDefault15Templates(member, category2);

            Page<Template> allBy = templateService.findAllBy(
                    member.getId(), "", category1.getId(), PageRequest.of(0, 20)
            );

            assertAll(() -> assertThat(allBy).hasSize(15),
                    () -> assertThat(allBy).allMatch(template -> template.getId() <= 15),
                    () -> assertThat(allBy.getTotalElements()).isEqualTo(15));
        }

        //        @Test
        //        @DisplayName("단일 태그 탐색 성공")
        //        void findBySingleTagPageSuccess() {
        //            Member member = memberRepository.fetchById(1L);
        //            Category category1 = categoryRepository.save(new Category("category1", member));
        //            tagRepository.save(new Tag("tag1"));
        //            tagRepository.save(new Tag("tag2"));
        //            saveDefault15Templates(member, category1);
        //            saveDefault15Templates(member, category1);
        //            for (long i = 1L; i <= 30L; i++) {
        //                templateTagRepository.save(
        //                        new TemplateTag(templateRepository.fetchById(i), tagRepository.fetchById((i % 2) +
        //                        1)));
        //            }
        //            FindAllTemplatesResponse allBy = templateService.findAllBy(
        //                    member.getId(), "", null, List.of(1L), DEFAULT_PAGING_REQUEST
        //            );
        //
        //            assertAll(() -> assertThat(allBy.templates()).hasSize(15),
        //                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() % 2 == 0), // 태그
        //                    () -> assertThat(allBy.totalElements()).isEqualTo(15));
        //        }
        //
        //        @Test
        //        @DisplayName("복수 태그 탐색 성공")
        //        void findByMultipleTagPageSuccess() {
        //            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        //            Member member = memberRepository.fetchById(memberDto.id());
        //            Category category1 = categoryRepository.save(new Category("category1", member));
        //            Category category2 = categoryRepository.save(new Category("category2", member));
        //            tagRepository.save(new Tag("tag1"));
        //            tagRepository.save(new Tag("tag2"));
        //            tagRepository.save(new Tag("tag3"));
        //            saveDefault15Templates(member, category1);
        //            saveDefault15Templates(member, category2);
        //            for (long i = 1L; i <= 30L; i++) {
        //                templateTagRepository.save(
        //                        new TemplateTag(templateRepository.fetchById(i), tagRepository.fetchById((i % 2) +
        //                        1)));
        //            }
        //
        //            for (long i = 1L; i <= 30L; i++) {
        //                templateTagRepository.save(
        //                        new TemplateTag(templateRepository.fetchById(i), tagRepository.fetchById(3L)));
        //            }
        //
        //            FindAllTemplatesResponse allBy = templateService.findAllBy(
        //                    member.getId(), "", null, List.of(1L, 3L), DEFAULT_PAGING_REQUEST
        //            );
        //
        //            assertAll(() -> assertThat(allBy.templates()).hasSize(15),
        //                    () -> assertThat(allBy.totalElements()).isEqualTo(15));
        //        }
        //
        //        @Test
        //        @DisplayName("카테고리 & 단일 태그 탐색 성공")
        //        void findByCategoryAndSingleTagPageSuccess() {
        //            MemberDto memberDto = MemberDtoFixture.getFirstMemberDto();
        //            Member member = memberRepository.fetchById(memberDto.id());
        //            Category category1 = categoryRepository.save(new Category("category1", member));
        //            Category category2 = categoryRepository.save(new Category("category2", member));
        //            saveDefault15Templates(member, category1);
        //            saveDefault15Templates(member, category2);
        //            tagRepository.save(new Tag("tag1"));
        //            tagRepository.save(new Tag("tag2"));
        //            for (long i = 1L; i <= 30L; i++) {
        //                templateTagRepository.save(
        //                        new TemplateTag(templateRepository.fetchById(i), tagRepository.fetchById((i % 2) +
        //                        1)));
        //            }
        //            FindAllTemplatesResponse allBy = templateService.findAllBy(
        //                    member.getId(), "", category1.getId(), List.of(1L), DEFAULT_PAGING_REQUEST
        //            );
        //
        //            assertAll(() -> assertThat(allBy.templates()).hasSize(7),
        //                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() < 16),
        //                    () -> assertThat(allBy.templates()).allMatch(template -> template.id() % 2 == 0),
        //                    () -> assertThat(allBy.totalElements()).isEqualTo(7));
        //        }
    }
}
