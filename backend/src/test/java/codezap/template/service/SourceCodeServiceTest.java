package codezap.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import codezap.fixture.CategoryFixture;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.fixture.SourceCodeFixture;
import codezap.template.fixture.TemplateFixture;
import codezap.template.repository.FakeSourceCodeRepository;
import codezap.template.repository.SourceCodeRepository;

class SourceCodeServiceTest {
    private final SourceCodeRepository sourceCodeRepository = new FakeSourceCodeRepository(
            List.of(SourceCodeFixture.getFirst(), SourceCodeFixture.getSecond(), SourceCodeFixture.getThird())
    );
    private final SourceCodeService sourceCodeService = new SourceCodeService(sourceCodeRepository);

    @Test
    @DisplayName("소스 코드 생성 성공")
    void createSourceCodesSuccess() {
        // given
        List<CreateSourceCodeRequest> sourceCodeRequest = List.of(
                new CreateSourceCodeRequest("filename1", "cotent", 1),
                new CreateSourceCodeRequest("filename2", "cotent", 2)
        );
        Template template = new Template(
                3L,
                MemberFixture.getFirstMember(),
                "new template",
                "description",
                CategoryFixture.getFirstCategory(),
                List.of()
        );

        // when
        sourceCodeService.createSourceCodes(template, sourceCodeRequest);
        List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);

        // then
        assertThat(sourceCodes).containsExactly(
                new SourceCode(4L, template, "filename1", "cotent", 1),
                new SourceCode(5L, template, "filename2", "cotent", 2)
        );
    }

    @Test
    @DisplayName("소스 코드 조회 성공")
    void getSourceCodeSuccess() {
        List<SourceCode> sourceCode = sourceCodeService.getSourceCode(TemplateFixture.getFirst());

        assertThat(sourceCode).containsExactly(SourceCodeFixture.getFirst(), SourceCodeFixture.getSecond());
    }

    @Test
    @DisplayName("소스 코드 수정 성공")
    void updateSourceCodesSuccess() {
        Template template = TemplateFixture.getFirst();
        List<UpdateSourceCodeRequest> updateSourceCodeRequests = List.of(
                new UpdateSourceCodeRequest(1L, "updateFilename", "updateContent", 1),
                new UpdateSourceCodeRequest(2L, "updateFilename2", "updateContent2", 2)
        );

        sourceCodeService.updateSourceCodes(updateSourceCodeRequests);
        List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);

        // then
        assertThat(sourceCodes).containsExactly(
                new SourceCode(1L, template, "updateFilename", "updateContent", 1),
                new SourceCode(2L, template, "updateFilename2", "updateContent2", 2)
        );
    }


    @Nested
    @DisplayName("소스 코드 개수 확인")
    class validateSourceCodesCount {
        @Test
        @DisplayName("소스 코드 개수 확인 성공")
        void validateSourceCodesCountSuccess() {
            Template template = TemplateFixture.getFirst();

            assertThatCode(() -> sourceCodeService.validateSourceCodesCount(template, 2))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("소스 코드 개수 확인 실패: 개수 다름")
        void validateSourceCodesCountFail() {
            Template template = TemplateFixture.getFirst();

            assertThatThrownBy(() -> sourceCodeService.validateSourceCodesCount(template, 3))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("소스 코드의 정보가 정확하지 않습니다.");
        }
    }


    @Nested
    @DisplayName("동일한 ID인 소스 코드 삭제")
    class testDeleteByIds {
        @Test
        @DisplayName("소스 코드 삭제 성공: 삭제 코드가 썸네일 일 경우")
        void testDeleteByIdsSuccessWithThumbnail() {
            Template template = TemplateFixture.getFirst();
            SourceCode sourceCode = SourceCodeFixture.getFirst();
            Thumbnail thumbnail = new Thumbnail(1L, template, sourceCode);

            sourceCodeService.deleteByIds(template, List.of(1L), thumbnail);
            List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);

            assertThat(sourceCodes).containsExactly(
                    new SourceCode(2L, template, "updateFilename2", "updateContent2", 2)
            );
        }

        @Test
        @DisplayName("소스 코드 삭제 성공: 삭제 소스 코드가 썸네일이 아닐 경우")
        void testDeleteByIdsSuccess() {
            Template template = TemplateFixture.getFirst();
            SourceCode sourceCode = SourceCodeFixture.getFirst();
            Thumbnail thumbnail = new Thumbnail(2L, template, sourceCode);

            sourceCodeService.deleteByIds(template, List.of(1L), thumbnail);
            List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);

            assertThat(sourceCodes).containsExactly(
                    new SourceCode(2L, template, "updateFilename2", "updateContent2", 2)
            );
        }
    }

    @Nested
    @DisplayName("템플릿 ID 소스 코드 삭제")
    class deleteByTemplateIds {

        @Test
        @DisplayName("소스 코드 삭제 성공")
        void deleteByTemplateIdsSuccess() {
            sourceCodeService.deleteByTemplateIds(List.of(
                    TemplateFixture.getFirst().getId(), TemplateFixture.getSecond().getId()
            ));
            List<SourceCode> sourceCodes = sourceCodeRepository.findAll();

            assertThat(sourceCodes).isEmpty();
        }
    }
}
