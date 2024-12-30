package codezap.template.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.request.validation.ValidatedSourceCodesCountRequest;
import codezap.template.dto.request.validation.ValidatedSourceCodesOrdinalRequest;
import codezap.template.repository.SourceCodeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SourceCodeService {

    private static final int MINIMUM_SOURCE_CODE_COUNT = 1;
    
    private final SourceCodeRepository sourceCodeRepository;

    @Transactional
    public void createSourceCodes(Template template, CreateTemplateRequest request) {
        validateSourceCodeCount(request);
        validateSourceCodesOrdinal(request);

        sourceCodeRepository.saveAll(
                request.sourceCodes().stream()
                        .map(createSourceCodeRequest -> createSourceCode(template, createSourceCodeRequest))
                        .toList()
        );
    }

    public SourceCode getByTemplateAndOrdinal(Template template, int ordinal) {
        return sourceCodeRepository.fetchByTemplateAndOrdinal(template, ordinal);
    }

    public List<SourceCode> findAllByTemplate(Template template) {
        return sourceCodeRepository.findAllByTemplate(template);
    }

    @Transactional
    public void updateSourceCodes(UpdateTemplateRequest request, Template template, Thumbnail thumbnail) {
        validateSourceCodeCount(request);
        validateSourceCodesOrdinal(request);

        request.updateSourceCodes().forEach(this::updateSourceCode);
        sourceCodeRepository.saveAll(
                request.createSourceCodes().stream()
                        .map(createSourceCodeRequest -> createSourceCode(template, createSourceCodeRequest))
                        .toList()
        );

        updateThumbnail(request, template, thumbnail);
        request.deleteSourceCodeIds().forEach(sourceCodeRepository::deleteById);

        validateSourceCodeCountMatch(template, request);
    }

    private void validateSourceCodeCount(ValidatedSourceCodesCountRequest request) {
        if(request.countSourceCodes() < MINIMUM_SOURCE_CODE_COUNT) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "소스 코드는 최소 1개 입력해야 합니다.");
        }
    }

    private void validateSourceCodesOrdinal(ValidatedSourceCodesOrdinalRequest request) {
        List<Integer> indexes = request.extractSourceCodesOrdinal();
        boolean isOrderValid = IntStream.range(0, indexes.size())
                .allMatch(index -> indexes.get(index) == index + 1);
        if(!isOrderValid) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "소스 코드 순서가 잘못되었습니다.");
        }
    }

    private void updateSourceCode(UpdateSourceCodeRequest request) {
        SourceCode sourceCode = sourceCodeRepository.fetchById(request.id());
        sourceCode.updateSourceCode(request.filename(), request.content(), request.ordinal());
    }

    private SourceCode createSourceCode(Template template, CreateSourceCodeRequest request) {
        return new SourceCode(template, request.filename(), request.content(), request.ordinal());
    }

    private void updateThumbnail(UpdateTemplateRequest request, Template template, Thumbnail thumbnail) {
        boolean isThumbnailDeleted = request.deleteSourceCodeIds()
                .contains(thumbnail.getSourceCode().getId());
        if (isThumbnailDeleted) {
            refreshThumbnail(template, thumbnail);
        }
    }

    private void refreshThumbnail(Template template, Thumbnail thumbnail) {
        List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplateAndOrdinal(
                template,
                thumbnail.getSourceCode().getOrdinal()
        );
        sourceCodes.stream()
                .filter(sourceCode -> !Objects.equals(thumbnail.getSourceCode(), sourceCode))
                .findFirst()
                .ifPresent(thumbnail::updateThumbnail);
    }

    private void validateSourceCodeCountMatch(Template template, UpdateTemplateRequest request) {
        if (request.countSourceCodes() != sourceCodeRepository.countByTemplate(template)) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "소스 코드의 정보가 정확하지 않습니다.");
        }
    }

    @Transactional
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        sourceCodeRepository.deleteAllByTemplateIds(templateIds);
    }
}
