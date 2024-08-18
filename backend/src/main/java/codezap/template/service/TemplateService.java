package codezap.template.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.template.domain.SourceCode;
import codezap.template.domain.Tag;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import codezap.template.dto.request.CreateSourceCodeRequest;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateSourceCodeRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.dto.response.ExploreTemplatesResponse;
import codezap.template.dto.response.FindTemplateResponse;
import codezap.template.repository.SourceCodeRepository;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.ThumbnailRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final ThumbnailRepository thumbnailRepository;
    private final TemplateRepository templateRepository;
    private final SourceCodeRepository sourceCodeRepository;

    public Template createTemplate(Member member, Category category, CreateTemplateRequest createTemplateRequest) {
        Template template = templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category)
        );
        sourceCodeRepository.saveAll(
                createTemplateRequest.sourceCodes().stream()
                        .map(createSourceCodeRequest -> createSourceCode(createSourceCodeRequest, template))
                        .toList()
        );
        SourceCode thumbnail = sourceCodeRepository.fetchByTemplateAndOrdinal(
                template, createTemplateRequest.thumbnailOrdinal());
        thumbnailRepository.save(new Thumbnail(template, thumbnail));
        return template;
    }

    private SourceCode createSourceCode(CreateSourceCodeRequest createSourceCodeRequest, Template template) {
        return new SourceCode(
                template, createSourceCodeRequest.filename(),
                createSourceCodeRequest.content(),
                createSourceCodeRequest.ordinal()
        );
    }

    public ExploreTemplatesResponse findAll() {
        return ExploreTemplatesResponse.from(thumbnailRepository.findAll());
    }

    public Template findByIdAndMember(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        validateTemplateAuthorizeMember(template, member);
        return template;
    }

    public FindTemplateResponse findSourceCode(Template template, List<Tag> tags) {
        List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplate(template);

        return FindTemplateResponse.of(template, sourceCodes, tags);
    }

    private void validateTemplateAuthorizeMember(Template template, Member member) {
        if (!template.getMember().equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 템플릿에 대한 권한이 없습니다.");
        }
    }

    public Page<Template> findAllBy(long memberId, String keyword, Pageable pageable) {
        keyword = "%" + keyword + "%";
        return templateRepository.searchBy(memberId, keyword, pageable);
    }

    public Page<Template> findAllBy(long memberId, String keyword, Long categoryId, Pageable pageable) {
        keyword = "%" + keyword + "%";
        return templateRepository.searchBy(memberId, keyword, categoryId, pageable);
    }

    public Page<Template> findAllBy(long memberId, String keyword, List<Long> templateIds, Pageable pageable) {
        keyword = "%" + keyword + "%";
        return templateRepository.searchBy(memberId, keyword, templateIds, pageable);
    }

    public Page<Template> findAllBy(
            long memberId, String keyword, Long categoryId, List<Long> templateIds, Pageable pageable
    ) {
        keyword = "%" + keyword + "%";
        return templateRepository.searchBy(memberId, keyword, categoryId, templateIds, pageable);
    }

    public List<Template> getByMemberId(Long memberId) {
        return templateRepository.findByMemberId(memberId);
    }

    public SourceCode getThumbnail(Template template) {
        return thumbnailRepository.fetchByTemplate(template).getSourceCode();
    }

    public Template update(Member member, Category category, Long templateId,
            UpdateTemplateRequest updateTemplateRequest
    ) {
        Template template = templateRepository.fetchById(templateId);
        validateTemplateAuthorizeMember(template, member);

        template.updateTemplate(updateTemplateRequest.title(), updateTemplateRequest.description(), category);
        updateSourceCodes(updateTemplateRequest, template);
        validateSourceCodesCount(updateTemplateRequest, template);

        return template;
    }

    private void updateSourceCodes(UpdateTemplateRequest updateTemplateRequest, Template template) {
        updateTemplateRequest.updateSourceCodes().forEach(this::updateSourceCode);
        sourceCodeRepository.saveAll(
                updateTemplateRequest.createSourceCodes().stream()
                        .map(createSourceCodeRequest -> createSourceCode(createSourceCodeRequest, template))
                        .toList()
        );

        Thumbnail thumbnail = thumbnailRepository.fetchByTemplate(template);

        if (isThumbnailDeleted(updateTemplateRequest, thumbnail)) {
            updateThumbnail(template, thumbnail);
        }

        updateTemplateRequest.deleteSourceCodeIds().forEach(sourceCodeRepository::deleteById);
    }

    private void updateSourceCode(UpdateSourceCodeRequest updateSourceCodeRequest) {
        SourceCode sourceCode = sourceCodeRepository.fetchById(updateSourceCodeRequest.id());
        sourceCode.updateSourceCode(updateSourceCodeRequest.filename(), updateSourceCodeRequest.content(),
                updateSourceCodeRequest.ordinal());
    }

    private boolean isThumbnailDeleted(
            UpdateTemplateRequest updateTemplateRequest,
            Thumbnail thumbnail
    ) {
        return updateTemplateRequest.deleteSourceCodeIds().contains(thumbnail.getId());
    }

    private void updateThumbnail(Template template, Thumbnail thumbnail) {
        List<SourceCode> sourceCodes = sourceCodeRepository.findAllByTemplateAndOrdinal(template,
                thumbnail.getSourceCode().getOrdinal());
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

    public void deleteByIds(Member member, List<Long> ids) {
        if (ids.size() != new HashSet<>(ids).size()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "삭제하고자 하는 템플릿 ID가 중복되었습니다.");
        }
        for (Long id : ids) {
            deleteById(member, id);
        }
    }

    private void deleteById(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        validateTemplateAuthorizeMember(template, member);

        thumbnailRepository.deleteByTemplateId(id);
        sourceCodeRepository.deleteByTemplateId(id);
        templateRepository.deleteById(id);
    }
}
