package codezap.template.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.TemplateRepository;
import codezap.template.repository.TemplateSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    @Transactional
    public Template create(Member member, CreateTemplateRequest createTemplateRequest, Category category) {
        Template template = new Template(
                member,
                createTemplateRequest.title(),
                createTemplateRequest.description(),
                category,
                createTemplateRequest.visibility());
        return templateRepository.save(template);
    }

    @Transactional(readOnly = true)
    public Template getById(Long id) {
        return templateRepository.fetchById(id);
    }

    @Transactional(readOnly = true)
    public List<Template> getByMemberId(Long memberId) {
        return templateRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Page<Template> findAllBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility,
            Pageable pageable
    ) {
        return templateRepository.findAll(
                new TemplateSpecification(memberId, keyword, categoryId, tagIds, visibility), pageable);
    }

    @Transactional
    public Template update(
            Member member,
            Long templateId,
            UpdateTemplateRequest updateTemplateRequest,
            Category category
    ) {
        Template template = templateRepository.fetchById(templateId);
        template.validateAuthorization(member);
        template.updateTemplate(
                updateTemplateRequest.title(),
                updateTemplateRequest.description(),
                category,
                updateTemplateRequest.visibility()
        );
        return template;
    }

    @Transactional
    public void deleteByMemberAndIds(Member member, List<Long> ids) {
        if (ids.size() != new HashSet<>(ids).size()) {
            throw new CodeZapException(ErrorCode.INVALID_REQUEST, "삭제하고자 하는 템플릿 ID가 중복되었습니다.");
        }
        ids.forEach(id -> deleteById(member, id));
    }

    private void deleteById(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        template.validateAuthorization(member);
        templateRepository.deleteById(id);
    }
}
