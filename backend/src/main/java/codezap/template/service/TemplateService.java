package codezap.template.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public Template createTemplate(Member member, CreateTemplateRequest createTemplateRequest, Category category) {
        Template template =
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category);
        return templateRepository.save(template);
    }

    public Template getByMemberAndId(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        template.validateAuthorization(member);
        return template;
    }

    public List<Template> getByMemberId(Long memberId) {
        return templateRepository.findByMemberId(memberId);
    }

    public Page<Template> findByMemberAndKeyword(Long memberId, String keyword, Pageable pageable) {
        keyword = "%" + keyword + "%";
        return templateRepository.searchBy(memberId, keyword, pageable);
    }

    public Page<Template> findByMemberKeywordAndCategory(
            Long memberId, String keyword, Long categoryId, Pageable pageable
    ) {
        keyword = "%" + keyword + "%";
        return templateRepository.searchBy(memberId, keyword, categoryId, pageable);
    }

    public Page<Template> findByMemberKeywordAndIds(
            Long memberId, String keyword, List<Long> templateIds, Pageable pageable
    ) {
        keyword = "%" + keyword + "%";
        return templateRepository.searchBy(memberId, keyword, templateIds, pageable);
    }

    public Page<Template> findByMemberKeywordCategoryAndIds(
            Long memberId, String keyword, Long categoryId, List<Long> templateIds, Pageable pageable
    ) {
        keyword = "%" + keyword + "%";
        return templateRepository.searchBy(memberId, keyword, categoryId, templateIds, pageable);
    }

    public Template updateTemplate(
            Member member,
            Long templateId,
            UpdateTemplateRequest updateTemplateRequest,
            Category category
    ) {
        Template template = templateRepository.fetchById(templateId);
        template.validateAuthorization(member);
        template.updateTemplate(updateTemplateRequest.title(), updateTemplateRequest.description(), category);
        return template;
    }

    @Transactional
    public void deleteByMemberAndIds(Member member, List<Long> ids) {
        if (ids.size() != new HashSet<>(ids).size()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "삭제하고자 하는 템플릿 ID가 중복되었습니다.");
        }
        ids.forEach(id -> deleteById(member, id));
    }

    private void deleteById(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        template.validateAuthorization(member);
        templateRepository.deleteById(id);
    }
}
