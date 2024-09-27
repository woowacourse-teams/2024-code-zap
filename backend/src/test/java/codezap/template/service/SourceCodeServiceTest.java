package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
            Template template = createSavedTemplate();
            CreateSourceCodeRequest request1 = new CreateSourceCodeRequest("file1.java", "content1", 1);
            CreateSourceCodeRequest request2 = new CreateSourceCodeRequest("file2.java", "content2", 2);

            // when
            sourceCodeService.createSourceCodes(template, List.of(request1, request2));

            // then
            List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);
            assertAll(
                    () -> assertThat(sourceCodes.size()).isEqualTo(2),
                    () -> assertThat(sourceCodes.get(0).getFilename()).isEqualTo("file1.java"),
                    () -> assertThat(sourceCodes.get(0).getOrdinal()).isEqualTo(1),
                    () -> assertThat(sourceCodes.get(1).getFilename()).isEqualTo("file2.java"),
                    () -> assertThat(sourceCodes.get(1).getOrdinal()).isEqualTo(2)
            );
        }

        @Test
        @Disabled("애플리케이션 코드에서 검증 코드 작성 필요")
        @DisplayName("실패: 순서 중복된 코드 존재")
        void createSourceCodes_WhenOrdinalIsDuplicate() {
            // given
            Template template = createSavedTemplate();
            int sameOrdinal = 1;
            CreateSourceCodeRequest request1 = new CreateSourceCodeRequest("file1.java", "content1", sameOrdinal);
            CreateSourceCodeRequest request2 = new CreateSourceCodeRequest("file2.java", "content2", sameOrdinal);

            // when
            sourceCodeService.createSourceCodes(template, List.of(request1, request2));

            // then
            assertThatThrownBy(() -> sourceCodeRepository.findAllByTemplate(template))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드의 순서는 중복될 수 없습니다.");
        }

        @Test
        @Disabled("애플리케이션 코드에서 검증 코드 작성 필요")
        @DisplayName("실패: 순서가 1부터 시작하지 않는 소스 코드")
        void createSourceCodes_WhenOrdinalIsNotStart1() {
            // given
            Template template = createSavedTemplate();
            CreateSourceCodeRequest request1 = new CreateSourceCodeRequest("file1.java", "content1", 0);
            CreateSourceCodeRequest request2 = new CreateSourceCodeRequest("file2.java", "content2", 1);

            // when
            sourceCodeService.createSourceCodes(template, List.of(request1, request2));

            // then
            assertThatThrownBy(() -> sourceCodeRepository.findAllByTemplate(template))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드의 순서는 1부터 시작해야 합니다.");
        }

        @Test
        @Disabled("애플리케이션 코드에서 검증 코드 작성 필요")
        @DisplayName("실패: 소스 코드의 순서들이 연속적이지 않은 경우")
        void createSourceCodes_WhenOrdinalIsNotSort() {
            // given
            Template template = createSavedTemplate();
            CreateSourceCodeRequest request1 = new CreateSourceCodeRequest("file1.java", "content1", 1);
            CreateSourceCodeRequest request2 = new CreateSourceCodeRequest("file2.java", "content2", 3);

            // when & then
            assertThatThrownBy(() -> sourceCodeService.createSourceCodes(template, List.of(request1, request2)))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드의 순서는 1부터 시작해야 합니다.");
        }
    }

    @Nested
    @DisplayName("템플릿과 순서에 해당하는 소스 코드 조회")
    class GetByTemplateAndOrdinal {

        @Test
        @DisplayName("성공")
        void getByTemplateAndOrdinal() {
            // given
            Template template = createSavedTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));

            // when & then
            assertAll(
                    () -> assertThat(sourceCodeService.getByTemplateAndOrdinal(template, 2)).isEqualTo(sourceCode2),
                    () -> assertThat(sourceCodeService.getByTemplateAndOrdinal(template, 1)).isEqualTo(sourceCode1));
        }

        @Test
        @DisplayName("실패: 해당 순서의 소스 코드 없음")
        void getByTemplateAndOrdinal_WhenOrdinalNotExist() {
            // given
            Template template = createSavedTemplate();
            sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            sourceCodeRepository.save(SourceCodeFixture.get(template, 2));

            // when & then
            assertThatThrownBy(() -> sourceCodeService.getByTemplateAndOrdinal(template, 3))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("템플릿에 3번째 소스 코드가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("템플릿에 해당하는 소스 코드 조회")
    class FindSourceCodesByTemplate {

        @Test
        @DisplayName("성공")
        void findSourceCodesByTemplate() {
            // given
            Template template = createSavedTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));

            // when & then
            assertThat(sourceCodeService.findAllByTemplate(template))
                    .containsExactly(sourceCode1, sourceCode2);
        }

        @Test
        @DisplayName("성공: 템플릿에 해당하는 소스 코드가 존재하지 않은 경우 빈 리스트 반환")
        void findSourceCodesByTemplate_WhenSourceCodeNotExist() {
            // given
            Template template = createSavedTemplate();

            // when & then
            assertThat(sourceCodeService.findAllByTemplate(template)).isEmpty();
        }
    }

    @Nested
    @DisplayName("소스 코드 수정")
    class UpdateSourceCodes {

        @Test
        @DisplayName("성공: 기존 소스 코드 제목, 내용 수정 및 새로운 소스 코드 추가")
        void updateSourceCodes() {
            // given
            Template template = createSavedTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest updateRequest1 = getUpdateSourceCodeRequest(sourceCode1);
            UpdateSourceCodeRequest updateRequest2 = getUpdateSourceCodeRequest(sourceCode2);
            CreateSourceCodeRequest createRequest = new CreateSourceCodeRequest("새로운 제목1", "새로운 내용1", 3);
            UpdateTemplateRequest updateTemplateRequest = getUpdateTemplateRequest(
                    List.of(createRequest),
                    List.of(updateRequest1, updateRequest2),
                    Collections.emptyList(),
                    template.getCategory().getId(),
                    Collections.emptyList()
            );

            // when
            sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);

            // then
            SourceCode updatedSourceCode1 = sourceCodeRepository.fetchById(sourceCode1.getId());
            SourceCode updatedSourceCode2 = sourceCodeRepository.fetchById(sourceCode2.getId());
            SourceCode newSourceCode = sourceCodeRepository.fetchByTemplateAndOrdinal(template, 3);

            assertAll(
                    () -> assertThat(sourceCodeRepository.countByTemplate(template)).isEqualTo(3),
                    () -> assertThat(updatedSourceCode1.getFilename()).isEqualTo("변경된 제목1"),
                    () -> assertThat(updatedSourceCode1.getOrdinal()).isEqualTo(1),
                    () -> assertThat(updatedSourceCode2.getFilename()).isEqualTo("변경된 제목2"),
                    () -> assertThat(updatedSourceCode2.getOrdinal()).isEqualTo(2),
                    () -> assertThat(newSourceCode.getFilename()).isEqualTo("새로운 제목1"));
        }

        @Test
        @Disabled("애플리케이션 코드에서 로직 변경 필요")
        @DisplayName("성공: 일부 소스 코드 삭제 및 새로운 소스 코드 추가 시, 삭제된 코드 순서는 앞당겨지고 새로 추가된 소스 코드의 순서는 가장 마지막 순서")
        void updateSourceCodes_WhenDeleteSomeAndAddNew_ExistingCodesHavePriority() {
            // given
            Template template = createSavedTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode deleteSourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest updateRequest1 = getUpdateSourceCodeRequest(sourceCode1);
            CreateSourceCodeRequest createRequest = new CreateSourceCodeRequest("새로운 제목1", "새로운 내용1", 3);
            UpdateTemplateRequest updateTemplateRequest = getUpdateTemplateRequest(
                    List.of(createRequest),
                    List.of(updateRequest1),
                    List.of(deleteSourceCode.getId()),
                    template.getCategory().getId(),
                    Collections.emptyList()
            );

            // when
            sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);

            // then
            assertAll(
                    () -> assertThat(sourceCodeRepository.countByTemplate(template)).isEqualTo(2),
                    () -> assertThat(sourceCodeRepository.fetchByTemplateAndOrdinal(template, 2).getFilename())
                            .isEqualTo("새로운 제목1"),
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
            Template template = createSavedTemplate();
            SourceCode thumbnailSourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode othersourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, thumbnailSourceCode));

            List<Long> deleteSourceCodeIds = List.of(thumbnailSourceCode.getId());
            UpdateSourceCodeRequest updateRequest2 = getUpdateSourceCodeRequest(othersourceCode);
            UpdateTemplateRequest updateTemplateRequest = getUpdateTemplateRequest(
                    Collections.emptyList(),
                    List.of(updateRequest2),
                    deleteSourceCodeIds,
                    template.getCategory().getId(),
                    Collections.emptyList()
            );

            // when
            sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);

            // then
            Thumbnail updatedThumbnail = thumbnailRepository.fetchByTemplate(template);
            assertAll(
                    () -> assertThat(updatedThumbnail.getSourceCode().getId()).isEqualTo(othersourceCode.getId()),
                    () -> assertThat(updatedThumbnail.getSourceCode().getOrdinal()).isEqualTo(1)
            );
        }

        @Test
        @DisplayName("성공: 소스 코드 추가 & 기존 소스 코드들 순서를 추가된 소스 코드보다 나중으로 변경")
        void updateSourceCodes_WhenChangeOrderToLast_AndAddNewCode() {
            // given
            Template template = createSavedTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest ordinalUpdateRequest = new UpdateSourceCodeRequest(sourceCode1.getId(), "변경된 제목1",
                    "변경된 내용1", 3);
            UpdateSourceCodeRequest updateRequest2 = getUpdateSourceCodeRequest(sourceCode2);
            CreateSourceCodeRequest createRequest = new CreateSourceCodeRequest("새로운 제목3", "새로운 내용3",
                    sourceCode1.getOrdinal());
            UpdateTemplateRequest updateTemplateRequest = getUpdateTemplateRequest(
                    List.of(createRequest),
                    List.of(ordinalUpdateRequest, updateRequest2),
                    Collections.emptyList(),
                    template.getCategory().getId(),
                    Collections.emptyList());

            // when
            sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail);

            // then
            assertAll(
                    () -> assertThat(sourceCodeRepository.countByTemplate(template)).isEqualTo(3),
                    () -> assertThat(sourceCodeRepository.fetchById(sourceCode1.getId()).getOrdinal()).isEqualTo(3),
                    () -> assertThat(sourceCodeRepository.fetchByTemplateAndOrdinal(template, 1).getFilename())
                            .isEqualTo("새로운 제목3")
            );
        }

        @Test
        @Disabled("현재는 전체 삭제를 막지 않고 thumbnail으로 인해 DataIntegrityViolationException 발생, 애플리케이션 코드에서 로직 변경 필요")
        @DisplayName("실패: 템플릿의 전체 소스 코드 삭제는 불가능함")
        void updateSourceCodes_WhenDeleteAll() {
            // given
            Template template = createSavedTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            List<Long> deleteSourceCodeIds = List.of(sourceCode1.getId(), sourceCode2.getId());

            UpdateTemplateRequest updateTemplateRequest = getUpdateTemplateRequest(
                    Collections.emptyList(),
                    Collections.emptyList(),
                    deleteSourceCodeIds,
                    template.getCategory().getId(),
                    Collections.emptyList()
            );

            // when & then
            assertThatThrownBy(() -> sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드는 최소 1개 이상 존재해야 합니다.");
        }

        @Test
        @DisplayName("실패: 추가가 아닌 모든 소스 코드는 추가 또는 삭제에 있어야 함")
        void updateSourceCodes_WhenNotContainsAny_UpdateOrDelete() {
            // given
            Template template = createSavedTemplate();
            SourceCode includedSourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode notIncludedSourceCode = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, includedSourceCode));

            UpdateSourceCodeRequest updateRequest1 = getUpdateSourceCodeRequest(includedSourceCode);
            UpdateTemplateRequest updateTemplateRequest = getUpdateTemplateRequest(
                    Collections.emptyList(),
                    List.of(updateRequest1),
                    Collections.emptyList(),
                    template.getCategory().getId(),
                    Collections.emptyList()
            );

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
            Template template = createSavedTemplate();
            SourceCode sourceCode1 = sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            SourceCode sourceCode2 = sourceCodeRepository.save(SourceCodeFixture.get(template, 2));
            Thumbnail thumbnail = thumbnailRepository.save(new Thumbnail(template, sourceCode1));

            UpdateSourceCodeRequest updateRequest1 = getUpdateSourceCodeRequest(sourceCode1);
            UpdateSourceCodeRequest updateRequest2 = getUpdateSourceCodeRequest(sourceCode2);

            UpdateTemplateRequest updateTemplateRequest = getUpdateTemplateRequest(
                    Collections.emptyList(),
                    List.of(updateRequest1, updateRequest2),
                    Collections.emptyList(),
                    template.getCategory().getId(),
                    Collections.emptyList()
            );

            // when & then
            assertThatThrownBy(() -> sourceCodeService.updateSourceCodes(updateTemplateRequest, template, thumbnail))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드의 순서는 중복될 수 없습니다.");
        }

        private UpdateSourceCodeRequest getUpdateSourceCodeRequest(SourceCode sourceCode) {
            return new UpdateSourceCodeRequest(
                    sourceCode.getId(),
                    "변경된 제목" + sourceCode.getOrdinal(),
                    "변경된 내용" + sourceCode.getOrdinal(),
                    sourceCode.getOrdinal()
            );
        }

        private UpdateTemplateRequest getUpdateTemplateRequest(
                List<CreateSourceCodeRequest> createSourceCodes,
                List<UpdateSourceCodeRequest> updateSourceCodes,
                List<Long> deleteSourceCodeIds,
                Long categoryId,
                List<String> tags
        ) {
            return new UpdateTemplateRequest(
                    "템플릿 수정",
                    "템플릿 설명",
                    createSourceCodes,
                    updateSourceCodes,
                    deleteSourceCodeIds,
                    categoryId,
                    tags);
        }
    }

    @Nested
    @DisplayName("id에 해당하는 모든 소스 코드 삭제")
    class DeleteByIds {

        @Test
        @DisplayName("성공")
        void deleteByIds() {
            // given
            Template template = createSavedTemplate();
            sourceCodeRepository.save(SourceCodeFixture.get(template, 1));
            sourceCodeRepository.save(SourceCodeFixture.get(template, 2));

            // when
            sourceCodeService.deleteByIds(List.of(template.getId()));

            // then
            assertThat(sourceCodeRepository.findAllByTemplate(template)).isEmpty();
        }

        @Test
        @DisplayName("성공: 소스 코드가 존재하지 않는 경우")
        void deleteByIds_WhenIdNotExist() {
            Template template = createSavedTemplate();

            sourceCodeService.deleteByIds(List.of(template.getId()));

            assertThat(sourceCodeRepository.findAllByTemplate(template)).isEmpty();
        }
    }

    private Template createSavedTemplate() {
        Member member = memberRepository.save(MemberFixture.getFirstMember());
        Category category = categoryRepository.save(CategoryFixture.getFirstCategory());
        return templateRepository.save(TemplateFixture.get(member, category));
    }
}
