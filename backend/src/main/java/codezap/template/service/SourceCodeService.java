package codezap.template.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public SourceCode createSourceCodes(
            List<CreateSourceCodeRequest> sourceCodes,
            int thumbnailOrdinal,
            Template template
    ) {
        sourceCodeRepository.saveAll(
                sourceCodes.stream()
                        .map(createSourceCodeRequest -> createSourceCode(createSourceCodeRequest, template))
                        .toList()
        );
        return sourceCodeRepository.fetchByTemplateAndOrdinal(template, thumbnailOrdinal);
    }

    private SourceCode createSourceCode(CreateSourceCodeRequest createSourceCodeRequest, Template template) {
        return new SourceCode(
                template,
                createSourceCodeRequest.filename(),
                createSourceCodeRequest.content(),
                createSourceCodeRequest.ordinal()
        );
    }

    public List<SourceCode> getSourceCode(Template template) {
        return sourceCodeRepository.findAllByTemplate(template);
    }

    public void updateSourceCodes(UpdateTemplateRequest updateTemplateRequest, Template template, Thumbnail thumbnail) {
        updateTemplateRequest.updateSourceCodes().forEach(this::updateSourceCode);
        sourceCodeRepository.saveAll(
                updateTemplateRequest.createSourceCodes().stream()
                        .map(createSourceCodeRequest -> createSourceCode(createSourceCodeRequest, template))
                        .toList()
        );

        if (isThumbnailDeleted(updateTemplateRequest, thumbnail)) {
            updateThumbnail(template, thumbnail);
        }
        updateTemplateRequest.deleteSourceCodeIds().forEach(sourceCodeRepository::deleteById);
        validateSourceCodesCount(updateTemplateRequest, template);
    }

    private void updateSourceCode(UpdateSourceCodeRequest updateSourceCodeRequest) {
        SourceCode sourceCode = sourceCodeRepository.fetchById(updateSourceCodeRequest.id());
        sourceCode.updateSourceCode(
                updateSourceCodeRequest.filename(),
                updateSourceCodeRequest.content(),
                updateSourceCodeRequest.ordinal()
        );
    }

    private boolean isThumbnailDeleted(
            UpdateTemplateRequest updateTemplateRequest,
            Thumbnail thumbnail
    ) {
        return updateTemplateRequest.deleteSourceCodeIds()
                .contains(thumbnail.getId());
    }

    private void updateThumbnail(Template template, Thumbnail thumbnail) {
        List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplateAndOrdinal(
                template,
                thumbnail.getSourceCode().getOrdinal()
        );
        sourceCodes.stream()
                .filter(sourceCode -> !Objects.equals(thumbnail.getSourceCode().getId(), sourceCode.getId()))
                .findFirst()
                .ifPresent(thumbnail::updateThumbnail);
    }

    private void validateSourceCodesCount(UpdateTemplateRequest updateTemplateRequest, Template template) {
        if (updateTemplateRequest.updateSourceCodes().size() + updateTemplateRequest.createSourceCodes().size()
                != sourceCodeRepository.findAllByTemplate(template).size()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "소스 코드의 정보가 정확하지 않습니다.");
        }
    }

    public void deleteByIds(List<Long> templateIds) {
        for (Long id : templateIds) {
            sourceCodeRepository.deleteByTemplateId(id);
        }
    }
}
