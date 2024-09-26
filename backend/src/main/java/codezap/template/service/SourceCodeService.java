package codezap.template.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.SourceCodeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SourceCodeService {

    private final SourceCodeRepository sourceCodeRepository;

    @Transactional
    public void createSourceCodes(Template template, List<CreateSourceCodeRequest> sourceCodes) {
        sourceCodeRepository.saveAll(
                sourceCodes.stream()
                        .map(createSourceCodeRequest -> createSourceCode(template, createSourceCodeRequest))
                        .toList()
        );
    }

    public SourceCode getByTemplateAndOrdinal(Template template, int ordinal) {
        return sourceCodeRepository.fetchByTemplateAndOrdinal(template, ordinal);
    }

    public List<SourceCode> findSourceCodesByTemplate(Template template) {
        return sourceCodeRepository.findAllByTemplate(template);
    }

    @Transactional
    public void updateSourceCodes(UpdateTemplateRequest updateTemplateRequest, Template template, Thumbnail thumbnail) {
        updateTemplateRequest.updateSourceCodes().forEach(this::updateSourceCode);
        sourceCodeRepository.saveAll(
                updateTemplateRequest.createSourceCodes().stream()
                        .map(createSourceCodeRequest -> createSourceCode(template, createSourceCodeRequest))
                        .toList()
        );

        updateThumbnail(updateTemplateRequest, template, thumbnail);
        updateTemplateRequest.deleteSourceCodeIds().forEach(sourceCodeRepository::deleteById);
        validateSourceCodesCount(template, updateTemplateRequest);
    }

    private void updateSourceCode(UpdateSourceCodeRequest updateSourceCodeRequest) {
        SourceCode sourceCode = sourceCodeRepository.fetchById(updateSourceCodeRequest.id());
        sourceCode.updateSourceCode(
                updateSourceCodeRequest.filename(),
                updateSourceCodeRequest.content(),
                updateSourceCodeRequest.ordinal()
        );
    }

    private SourceCode createSourceCode(Template template, CreateSourceCodeRequest createSourceCodeRequest) {
        return new SourceCode(
                template,
                createSourceCodeRequest.filename(),
                createSourceCodeRequest.content(),
                createSourceCodeRequest.ordinal()
        );
    }

    private void updateThumbnail(UpdateTemplateRequest updateTemplateRequest, Template template, Thumbnail thumbnail) {
        boolean isThumbnailDeleted = updateTemplateRequest.deleteSourceCodeIds()
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

    private void validateSourceCodesCount(Template template, UpdateTemplateRequest updateTemplateRequest) {
        if (updateTemplateRequest.updateSourceCodes().size() + updateTemplateRequest.createSourceCodes().size()
                != sourceCodeRepository.countByTemplate(template)) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "소스 코드의 정보가 정확하지 않습니다.");
        }
    }

    @Transactional
    public void deleteByIds(List<Long> templateIds) {
        templateIds.forEach(sourceCodeRepository::deleteByTemplateId);
    }
}
