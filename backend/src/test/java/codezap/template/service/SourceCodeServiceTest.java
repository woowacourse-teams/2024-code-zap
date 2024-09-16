package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThatList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Disabled;
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
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;

class SourceCodeServiceTest extends ServiceTest {

    @Autowired
    private SourceCodeService sourceCodeService;

    @Nested
    @DisplayName("소스 코드 생성")
    class CreateSourceCodes {

        @Test
        @DisplayName("성공: 템플릿에 해당하는 소스 코드 저장")
        void createSourceCodes() {
            // given
            Template template = createTemplate();
            CreateSourceCodeRequest request1 = new CreateSourceCodeRequest("file1.java", "content1", 1);
            CreateSourceCodeRequest request2 = new CreateSourceCodeRequest("file2.java", "content2", 2);

            // when
            sourceCodeService.createSourceCodes(template, List.of(request1, request2));

            // then
            List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);
            assertAll(() -> assertEquals(2, sourceCodes.size()),
                    () -> assertEquals("file1.java", sourceCodes.get(0).getFilename()),
                    () -> assertEquals("content1", sourceCodes.get(0).getContent()),
                    () -> assertEquals(1, sourceCodes.get(0).getOrdinal()),
                    () -> assertEquals("file2.java", sourceCodes.get(1).getFilename()),
                    () -> assertEquals("content2", sourceCodes.get(1).getContent()),
                    () -> assertEquals(2, sourceCodes.get(1).getOrdinal()));
        }

        @Test
        @Disabled("애플리케이션 코드에서 검증 코드 작성 필요")
        @DisplayName("실패: 순서 중복된 코드 존재")
        void createSourceCodes_WhenOrdinalIsDuplicate() {
            // given
            Template template = createTemplate();
            int sameOrdinal = 1;
            CreateSourceCodeRequest request1 = new CreateSourceCodeRequest("file1.java", "content1", sameOrdinal);
            CreateSourceCodeRequest request2 = new CreateSourceCodeRequest("file2.java", "content2", sameOrdinal);

            // when
            sourceCodeService.createSourceCodes(template, List.of(request1, request2));

            // then
            assertThatThrownBy(() -> sourceCodeRepository.findAllByTemplate(template)).isInstanceOf(
                    CodeZapException.class).hasMessage("소스 코드의 순서는 중복될 수 없습니다.");
        }

        @Test
        @Disabled("애플리케이션 코드에서 검증 코드 작성 필요")
        @DisplayName("실패: 순서가 1부터 시작하지 않는 코드")
        void createSourceCodes_WhenOrdinalIsNotStart1() {
            // given
            Template template = createTemplate();
            CreateSourceCodeRequest request1 = new CreateSourceCodeRequest("file1.java", "content1", 0);
            CreateSourceCodeRequest request2 = new CreateSourceCodeRequest("file2.java", "content2", 1);

            // when
            sourceCodeService.createSourceCodes(template, List.of(request1, request2));

            // then
            assertThatThrownBy(() -> sourceCodeRepository.findAllByTemplate(template)).isInstanceOf(
                    CodeZapException.class).hasMessage("소스 코드의 순서는 1부터 시작해야 합니다.");
        }
    }

    @Nested
    @DisplayName("템플릿과 순서에 해당하는 소스 코드 조회")
    class getByTemplateAndOrdinal {

        @Test
        @DisplayName("성공")
        void getByTemplateAndOrdinal() {
            // given
            Template template = createTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));

            // when & then
            assertAll(() -> assertEquals(sourceCodeService.getByTemplateAndOrdinal(template, 2), sourceCode2),
                    () -> assertEquals(sourceCodeService.getByTemplateAndOrdinal(template, 1), sourceCode1));
        }

        @Test
        @DisplayName("실패: 템플릿에 해당하는 소스 코드가 존재하지 않음")
        void getByTemplateAndOrdinal_WhenSourceCodeNotExist() {
            // given
            Template template = createTemplate();

            // when & then
            assertThatThrownBy(() -> sourceCodeService.getByTemplateAndOrdinal(template, 1)).isInstanceOf(
                    CodeZapException.class).hasMessage("템플릿에 1번째 소스 코드가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("실패: 해당 순서의 소스 코드 없음")
        void getByTemplateAndOrdinal_WhenOrdinalNotExist() {
            // given
            Template template = createTemplate();
            sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            sourceCodeRepository.save(SourceCodeFixture.get(template, 2));

            // when & then
            assertThatThrownBy(() -> sourceCodeService.getByTemplateAndOrdinal(template, 3)).isInstanceOf(
                    CodeZapException.class).hasMessage("템플릿에 3번째 소스 코드가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿에 해당하는 소스 코드 조회")
    class findSourceCodesByTemplate {

        @Test
        @DisplayName("성공")
        void findSourceCodesByTemplate() {
            // given
            Template template = createTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));

            // when & then
            assertThatList(sourceCodeService.findSourceCodesByTemplate(template)).containsExactly(sourceCode1,
                    sourceCode2);
        }

        @Test
        @DisplayName("성공: 템플릿에 해당하는 소스 코드가 존재하지 않은 경우 빈 리스트 반환")
        void findSourceCodesByTemplate_WhenSourceCodeNotExist() {
            // given
            Template template = createTemplate();

            // when & then
            assertThatList(sourceCodeService.findSourceCodesByTemplate(template)).isEmpty();
        }
    }

    @Nested
    @DisplayName("소스 코드 수정")
    class updateSourceCodes {

        @Test
        @DisplayName("성공: 기존 소스 코드 제목, 내용 수정 및 새로운 소스 코드 추가")
        void updateSourceCodes() {
            // given
            Template template = createTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest updateRequest1 = new UpdateSourceCodeRequest(sourceCode1.getId(), "변경된 제목1",
                    "변경된 내용1", sourceCode1.getOrdinal());
            UpdateSourceCodeRequest updateRequest2 = new UpdateSourceCodeRequest(sourceCode2.getId(), "변경된 제목2",
                    "변경된 내용2", sourceCode2.getOrdinal());
            CreateSourceCodeRequest createRequest = new CreateSourceCodeRequest("새로운 제목1", "새로운 내용1", 3);
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest("템플릿 수정", "템플릿 설명",
                    List.of(createRequest), List.of(updateRequest1, updateRequest2), Collections.emptyList(),
                    template.getCategory().getId(), Collections.emptyList());

            // when
            sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);

            // then
            SourceCode updatedSourceCode1 = sourceCodeRepository.fetchById(sourceCode1.getId());
            SourceCode updatedSourceCode2 = sourceCodeRepository.fetchById(sourceCode2.getId());
            SourceCode newSourceCode = sourceCodeRepository.fetchByTemplateAndOrdinal(template, 3);

            assertAll(() -> assertEquals(3, sourceCodeRepository.countByTemplate(template)),
                    () -> assertEquals("변경된 제목1", updatedSourceCode1.getFilename()),
                    () -> assertEquals(1, updatedSourceCode1.getOrdinal()),
                    () -> assertEquals("변경된 제목2", updatedSourceCode2.getFilename()),
                    () -> assertEquals(2, updatedSourceCode2.getOrdinal()),
                    () -> assertEquals("새로운 제목1", newSourceCode.getFilename()));
        }

        @Test
        @Disabled("애플리케이션 코드에서 로직 변경 필요")
        @DisplayName("성공: 일부 소스 코드 삭제 및 새로운 소스 코드 추가 시, 삭제된 코드 순서는 앞당겨지고 새로 추가된 소스 코드의 순서는 가장 마지막 순서")
        void updateSourceCodes_WhenDeleteSomeAndAddNew_ExistingCodesHavePriority() {
            // given
            Template template = createTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode deleteSourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest updateRequest1 = new UpdateSourceCodeRequest(sourceCode1.getId(), "변경된 제목1",
                    "변경된 내용1", sourceCode1.getOrdinal());
            CreateSourceCodeRequest createRequest = new CreateSourceCodeRequest("새로운 제목1", "새로운 내용1", 3);
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest("템플릿 수정", "템플릿 설명",
                    List.of(createRequest), List.of(updateRequest1), List.of(deleteSourceCode.getId()),
                    template.getCategory().getId(), Collections.emptyList());

            // when
            sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);

            // then
            assertAll(
                    () -> assertEquals(2, sourceCodeRepository.countByTemplate(template)),
                    () -> assertEquals("새로운 제목1",
                            sourceCodeRepository.fetchByTemplateAndOrdinal(template, 2).getFilename()),
                    () -> assertThatThrownBy(() -> sourceCodeRepository.fetchById(deleteSourceCode.getId()))
                            .isInstanceOf(CodeZapException.class)
                            .hasMessage("식별자 " + deleteSourceCode.getId() + "에 해당하는 소스 코드가 존재하지 않습니다.")
            );
        }

        @Test
        @Disabled("애플리케이션 코드에서 로직 변경 필요")
        @DisplayName("성공: 썸네일 코드 삭제 시, 새로 순서가 1인 코드가 썸네일으로 등록")
        void updateSourceCodes_WhenDeleteThumbnailCode_NewThumbnailAssigned() {
            // given
            Template template = createTemplate();
            SourceCode thumbnailSourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, thumbnailSourceCode));

            List<Long> deleteSourceCodeIds = List.of(thumbnailSourceCode.getId());
            UpdateSourceCodeRequest updateRequest2 = new UpdateSourceCodeRequest(sourceCode2.getId(), "변경된 제목2",
                    "변경된 내용2", sourceCode2.getOrdinal());
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest("템플릿 수정", "템플릿 설명",
                    Collections.emptyList(), List.of(updateRequest2), deleteSourceCodeIds,
                    template.getCategory().getId(), Collections.emptyList());

            // when
            sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);

            // then
            Thumbnail updatedThumbnail = thumbnailRepository.fetchByTemplate(template);
            assertAll(
                    () -> assertEquals(sourceCode2.getId(), updatedThumbnail.getSourceCode().getId()),
                    () -> assertEquals(1, updatedThumbnail.getSourceCode().getOrdinal())
            );
        }

        @Test
        @DisplayName("성공: 소스 코드 추가 및 기존 소스 코드 중 순서를 추가된 소스 코드보다 더 가장 나중으로 변경 및 소스 코드 추가")
        void updateSourceCodes_WhenChangeOrderToLast_AndAddNewCode() {
            // given
            Template template = createTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest updateRequest1 = new UpdateSourceCodeRequest(sourceCode1.getId(), "변경된 제목1",
                    "변경된 내용1", 3);
            UpdateSourceCodeRequest updateRequest2 = new UpdateSourceCodeRequest(sourceCode2.getId(), "변경된 제목2",
                    "변경된 내용2", 2);
            CreateSourceCodeRequest createRequest = new CreateSourceCodeRequest("새로운 제목3", "새로운 내용3", 1);
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest("템플릿 수정", "템플릿 설명",
                    List.of(createRequest), List.of(updateRequest1, updateRequest2), Collections.emptyList(),
                    template.getCategory().getId(), Collections.emptyList());

            // when
            sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);

            // then
            assertAll(() -> assertEquals(3, sourceCodeRepository.countByTemplate(template)),
                    () -> assertEquals(3, sourceCodeRepository.fetchById(sourceCode1.getId()).getOrdinal()),
                    () -> assertEquals("새로운 제목3",
                            sourceCodeRepository.fetchByTemplateAndOrdinal(template, 1).getFilename())
            );
        }

        @Test
        @Disabled("현재는 전체 삭제를 막지 않고 thumbnail으로 인해 DataIntegrityViolationException 발생, 애플리케이션 코드에서 로직 변경 필요")
        @DisplayName("실패: 소스 코드 전체 삭제")
        void updateSourceCodes_WhenDeleteAll() {
            // given
            Template template = createTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            List<Long> deleteSourceCodeIds = List.of(sourceCode1.getId(), sourceCode2.getId());

            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest("템플릿 수정", "템플릿 설명",
                    Collections.emptyList(), Collections.emptyList(), deleteSourceCodeIds,
                    template.getCategory().getId(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드는 최소 1개 이상 존재해야 합니다.");
        }

        @Test
        @DisplayName("실패: 추가가 아닌 모든 소스 코드는 추가 또는 삭제에 있어야 함")
        void updateSourceCodes_WhenNotContainsAny_UpdateOrDelete() {
            // given
            Template template = createTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest updateRequest1 = new UpdateSourceCodeRequest(sourceCode1.getId(), "변경된 제목1",
                    "변경된 내용1", 1);
            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest("템플릿 수정", "템플릿 설명",
                    Collections.emptyList(), List.of(updateRequest1), Collections.emptyList(),
                    template.getCategory().getId(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드의 정보가 정확하지 않습니다.");
        }

        @Test
        @Disabled("중복이어도 저장되고 있음, 애플리케이션 코드에서 로직 변경 필요")
        @DisplayName("실패: 변경할 소스 코드의 순서가 중복된 소스 코드의 순서인 경우")
        void updateSourceCodes_WhenDuplicateOrder() {
            // given
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template template = templateRepository.save(TemplateFixture.get(member, category));
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest updateRequest1 = new UpdateSourceCodeRequest(sourceCode1.getId(), "변경된 제목1",
                    "변경된 내용1", 2);
            UpdateSourceCodeRequest updateRequest2 = new UpdateSourceCodeRequest(sourceCode2.getId(), "변경된 제목2",
                    "변경된 내용2", 2);

            UpdateTemplateRequest updateTemplateRequest = new UpdateTemplateRequest("템플릿 수정", "템플릿 설명",
                    Collections.emptyList(), List.of(updateRequest1, updateRequest2), Collections.emptyList(),
                    template.getCategory().getId(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드의 순서는 중복될 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("id에 해당하는 모든 소스 코드 삭제")
    class deleteByIds {

        @Test
        @DisplayName("성공")
        void deleteByIds() {
            // given
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template template = templateRepository.save(TemplateFixture.get(member, category));
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));

            // when
            sourceCodeService.deleteByIds(List.of(template.getId()));

            // then
            assertThatList(sourceCodeRepository.findAllByTemplate(template)).isEmpty();
        }

        @Test
        @DisplayName("성공: 소스 코드가 존재하지 않는 경우")
        void deleteByIds_WhenIdNotExist() {
            // given
            Member member = memberRepository.save(MemberFixture.getFirstMember());
            Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
            Template template = templateRepository.save(TemplateFixture.get(member, category));

            // when
            sourceCodeService.deleteByIds(List.of(template.getId()));

            // then
            assertThatList(sourceCodeRepository.findAllByTemplate(template)).isEmpty();
        }
    }

    private Template createTemplate() {
        Member member = memberRepository.save(MemberFixture.getFirstMember());
        Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
        return templateRepository.save(TemplateFixture.get(member, category));
    }
}
