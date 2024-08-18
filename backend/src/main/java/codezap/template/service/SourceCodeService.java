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
import codezap.template.repository.SourceCodeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SourceCodeService {
    private final SourceCodeRepository sourceCodeRepository;

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

    public List<SourceCode> getSourceCode(Template template) {
        return sourceCodeRepository.findAllByTemplate(template);
    }

    public void updateSourceCodes(List<UpdateSourceCodeRequest> updateSourceCodes) {
        updateSourceCodes.forEach(this::updateSourceCode);
    }

    private SourceCode createSourceCode(Template template, CreateSourceCodeRequest createSourceCodeRequest) {
        return new SourceCode(
                template,
                createSourceCodeRequest.filename(),
                createSourceCodeRequest.content(),
                createSourceCodeRequest.ordinal()
        );
    }

    private void updateSourceCode(UpdateSourceCodeRequest updateSourceCodeRequest) {
        SourceCode sourceCode = sourceCodeRepository.fetchById(updateSourceCodeRequest.id());
        sourceCode.updateSourceCode(
                updateSourceCodeRequest.filename(),
                updateSourceCodeRequest.content(),
                updateSourceCodeRequest.ordinal()
        );
    }

    public void validateSourceCodesCount(Template template, int sourceCodeSize) {
        if (sourceCodeSize != sourceCodeRepository.findAllByTemplate(template).size()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "소스 코드의 정보가 정확하지 않습니다.");
        }
    }

    public void deleteByIds(Template template, List<Long> ids, Thumbnail thumbnail) {
        if (isThumbnailDeleted(ids, thumbnail)) {
            updateThumbnail(template, thumbnail);
        }
        ids.forEach(sourceCodeRepository::deleteById);
        for (Long id : ids) {
            sourceCodeRepository.deleteById(id);
        }
    }

    private boolean isThumbnailDeleted(
            List<Long> deleteSourceCodeIds,
            Thumbnail thumbnail
    ) {
        return deleteSourceCodeIds.contains(thumbnail.getId());
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

    public void deleteByTemplateIds(List<Long> templateIds) {
        for (Long id : templateIds) {
            sourceCodeRepository.deleteByTemplateId(id);
        }
    }
}
