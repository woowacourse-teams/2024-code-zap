package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import codezap.category.domain.Category;
import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.fixture.TemplateFixture;
import codezap.template.repository.FakeSourceCodeRepository;
import codezap.template.repository.FakeTemplateRepository;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;

class TemplateServiceTest {
    private final TemplateRepository templateRepository = new FakeTemplateRepository(
            List.of(TemplateFixture.getFirst(), TemplateFixture.getSecond(), TemplateFixture.getThird())
    );
    private final SourceCodeRepository sourceCodeRepository = new FakeSourceCodeRepository();
    private final TemplateService templateService = new TemplateService(templateRepository);

    @Test
    @DisplayName("템플릿 생성 성공")
    void createTemplateSuccess() {
        Member member = MemberFixture.getFirstMember();
        Category category = CategoryFixture.getFirstCategory();

        Template actual = templateService.createTemplate(member, "title", "description", category);
        Template expected = templateRepository.fetchById(actual.getId());

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(templateRepository.findAll()).hasSize(4),
                () -> assertThat(actual.getTitle()).isEqualTo("title"),
                () -> assertThat(actual.getCategory().getName()).isEqualTo("카테고리 없음")
        );
    }

    @Nested
    @DisplayName("멤버와 ID로 템플릿 조회")
    class getByMemberIdAndId {
        @Test
        @DisplayName("멤버와 ID로 템플릿 조회 성공")
        void getByMemberAndIdSuccess() {
            Member member = MemberFixture.getFirstMember();

            Template actual = templateService.getByMemberAndId(member, 1L);
            Template expected = templateRepository.fetchById(1L);

            assertAll(
                    () -> assertThat(actual).isEqualTo(expected),
                    () -> assertThat(actual.getMember()).isEqualTo(member)
            );
        }

        @Test
        @DisplayName("멤버와 ID로 템플릿 조회 실패: 권한 없음")
        void getByMemberAndIdFailNotMatch() {
            Member member = MemberFixture.getFirstMember();

            assertThatThrownBy(() -> templateService.getByMemberAndId(member, 3L))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("멤버 ID로 템플릿 조회")
    class getByMemberId {
        @Test
        @DisplayName("멤버 ID로 템플릿 조회 성공")
        void getByMemberIdSuccess() {
            Member member = MemberFixture.getFirstMember();

            List<Template> templates = templateService.getByMemberId(member.getId());

            assertThat(templates).containsExactly(
                    TemplateFixture.getFirst(), TemplateFixture.getSecond()
            );
        }
    }

    @Nested
    @DisplayName("템플릿 검색")
    class findAllBy {

        @Nested
        @DisplayName("템플릿 검색: 토픽")
        class findAllByOnlyTopic {
            @Test
            @DisplayName("템플릿 토픽 검색 성공 : 템플릿 제목에 포함")
            void findAllTemplatesTitleContainKeywordSuccess() {
                //given
                Member member = MemberFixture.getFirstMember();
                Category category = CategoryFixture.getFirstCategory();
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
                Member member = MemberFixture.getFirstMember();
                Category category = CategoryFixture.getFirstCategory();

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
                Member member = MemberFixture.getFirstMember();
                Category category = CategoryFixture.getFirstCategory();

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
                Member member = MemberFixture.getFirstMember();
                Category category = CategoryFixture.getFirstCategory();

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

            private void saveTemplateByFilename(String templateTitle, String firstFilename, String secondFilename,
                    Member member, Category category
            ) {
                Template savedTemplate = templateRepository.save(new Template(member, templateTitle, "설명", category));
                SourceCode savedFirstSourceCode = sourceCodeRepository.save(
                        new SourceCode(savedTemplate, firstFilename, "content1", 1));
                SourceCode savedSecondSourceCode = sourceCodeRepository.save(
                        new SourceCode(savedTemplate, secondFilename, "content2", 2));
                savedTemplate.updateSourceCodes(List.of(savedFirstSourceCode, savedSecondSourceCode));
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
            }
        }

        @Nested
        @DisplayName("템플릿 검색: 페이징")
        class findAllByPage {

            @Test
            @DisplayName("템플릿 페이징: 1페이지 성공")
            void findAllFirstPageSuccess() {
                //given
                Member member = MemberFixture.getFirstMember();
                Category category1 = CategoryFixture.getFirstCategory();
                Category category2 = CategoryFixture.getSecondCategory();
                saveDefault15Templates(member, category1);
                saveDefault15Templates(member, category2);

                Page<Template> allBy = templateService.findAllBy(
                        member.getId(), "hello keyword", PageRequest.of(0, 20)
                );

                //when & then
                assertAll(
                        () -> assertThat(allBy).hasSize(20),
                        () -> assertThat(allBy).allMatch(template -> template.getId() <= 23),
                        () -> assertThat(allBy.getTotalElements()).isEqualTo(30));
            }

            @Test
            @DisplayName("템플릿 페이징: 2페이지 성공")
            void findAllSecondPageSuccess() {
                //given
                Member member = MemberFixture.getFirstMember();
                Category category1 = CategoryFixture.getFirstCategory();
                Category category2 = CategoryFixture.getSecondCategory();
                saveDefault15Templates(member, category1);
                saveDefault15Templates(member, category2);

                Page<Template> allBy = templateService.findAllBy(
                        member.getId(), "hello keyword", PageRequest.of(1, 20)
                );

                assertAll(() -> assertThat(allBy).hasSize(10),
                        () -> assertThat(allBy).allMatch(template -> template.getId() > 23),
                        () -> assertThat(allBy.getTotalElements()).isEqualTo(30));
            }
        }

        @Nested
        @DisplayName("템플릿 검색: 카테고리")
        class findAllByCategory {
            @Test
            @DisplayName("카테고리 검색 성공")
            void findByCategoryPageSuccess() {
                Member member = MemberFixture.getFirstMember();
                Category category1 = CategoryFixture.getFirstCategory();
                Category category2 = CategoryFixture.getSecondCategory();
                saveDefault15Templates(member, category1);
                saveDefault15Templates(member, category2);

                Page<Template> allBy = templateService.findAllBy(
                        member.getId(), "hello keyword", category1.getId(), PageRequest.of(0, 20)
                );

                assertAll(() -> assertThat(allBy).hasSize(15),
                        () -> assertThat(allBy).allMatch(template -> template.getId() <= 18),
                        () -> assertThat(allBy.getTotalElements()).isEqualTo(15));
            }
        }

        @Nested
        @DisplayName("템플릿 검색: 태그가 포함된 템플릿")
        class findAllByTag {
            @Test
            @DisplayName("템플릿 ID 검색 성공")
            void findBySingleTagPageSuccess() {
                Member member = MemberFixture.getFirstMember();
                Category category = CategoryFixture.getFirstCategory();
                saveDefault15Templates(member, category);

                Page<Template> allBy = templateService.findAllBy(
                        member.getId(),
                        "hello keyword",
                        List.of(4L, 5L, 10L, 13L, 15L),
                        PageRequest.of(0, 3)
                );

                assertAll(() -> assertThat(allBy).hasSize(3),
                        () -> assertThat(allBy).extracting("id").containsExactly(4L, 5L, 10L),
                        () -> assertThat(allBy.getTotalElements()).isEqualTo(5));
            }
        }

        @Nested
        @DisplayName("템플릿 검색: 카테고리, 태그가 포함된 템플릿")
        class findAllByCategoryAndTag {
            @Test
            @DisplayName("템플릿 검색 성공")
            void findByCategoryAndTagSuccess() {
                Member member = MemberFixture.getFirstMember();
                Category category1 = CategoryFixture.getFirstCategory();
                Category category2 = CategoryFixture.getSecondCategory();
                saveDefault15Templates(member, category1);
                saveDefault15Templates(member, category2);

                Page<Template> allBy = templateService.findAllBy(
                        member.getId(),
                        "hello keyword",
                        1L,
                        List.of(4L, 5L, 20L),
                        PageRequest.of(0, 3)
                );

                assertAll(() -> assertThat(allBy).hasSize(2),
                        () -> assertThat(allBy).extracting("id").containsExactly(4L, 5L),
                        () -> assertThat(allBy.getTotalElements()).isEqualTo(2));
            }
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

        private Template saveTemplate(CreateTemplateRequest createTemplateRequest, Member member, Category category) {
            return templateRepository.save(
                    new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category)
            );
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

    @Nested
    @DisplayName("템플릿 수정")
    class updateTemplate {

        @Test
        @DisplayName("템플릿 수정 성공")
        void updateTemplateSuccess() {
            // given
            Member member = MemberFixture.getFirstMember();
            Category category = CategoryFixture.getSecondCategory();
            Template template = TemplateFixture.getFirst();

            // when
            templateService.updateTemplate(
                    member, template.getId(), "updateTitle", "updateDescription", category
            );
            Template updateTemplate = templateRepository.fetchById(template.getId());

            // then
            assertAll(
                    () -> assertThat(updateTemplate.getTitle()).isEqualTo("updateTitle"),
                    () -> assertThat(updateTemplate.getDescription()).isEqualTo("updateDescription"),
                    () -> assertThat(updateTemplate.getCategory().getId()).isEqualTo(category.getId())
            );
        }

        @Test
        @DisplayName("템플릿 수정 실패: 권한 없음")
        void updateTemplateFailWithUnauthorized() {
            // given
            Category category = CategoryFixture.getSecondCategory();
            Template template = TemplateFixture.getFirst();
            Long templateId = template.getId();
            Member otherMember = MemberFixture.getSecondMember();

            // then
            assertThatThrownBy(() -> templateService.updateTemplate(
                    otherMember, templateId, "updateTitle", "updateDescription", category
            ))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿 삭제")
    class deleteByMemberAndIds {

        @Test
        @DisplayName("템플릿 삭제 성공: 1개")
        void deleteTemplateSuccess() {
            // given
            Member member = MemberFixture.getFirstMember();

            // when
            templateService.deleteByMemberAndIds(
                    member,
                    List.of(TemplateFixture.getFirst().getId())
            );

            // then
            assertThat(templateRepository.findAll()).hasSize(2);
        }

        @Test
        @DisplayName("템플릿 삭제 성공: 2개")
        void deleteTemplatesSuccess() {
            // given
            Member member = MemberFixture.getFirstMember();

            // when
            templateService.deleteByMemberAndIds(
                    member,
                    List.of(TemplateFixture.getFirst().getId(), TemplateFixture.getSecond().getId())
            );

            // then
            assertThat(templateRepository.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 권한 없음")
        void deleteTemplateFailWithUnauthorized() {
            // given
            Member otherMember = MemberFixture.getFirstMember();

            // then
            List<Long> ids = List.of(TemplateFixture.getThird().getId());
            assertThatThrownBy(() -> templateService.deleteByMemberAndIds(otherMember, ids))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("해당 템플릿에 대한 권한이 없습니다.");
        }

        @Test
        @DisplayName("템플릿 삭제 실패: 동일한 ID 2개가 들어왔을 때")
        void deleteTemplatesFailWithSameTemplateId() {
            // given
            Member member = MemberFixture.getFirstMember();

            // when & then
            List<Long> ids = List.of(TemplateFixture.getFirst().getId(), TemplateFixture.getFirst().getId());
            assertThatThrownBy(() -> templateService.deleteByMemberAndIds(member, ids))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("삭제하고자 하는 템플릿 ID가 중복되었습니다.");
        }
    }
}
