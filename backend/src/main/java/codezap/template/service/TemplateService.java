package codezap.template.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
        return templateRepository.save(
                new Template(member, createTemplateRequest.title(), createTemplateRequest.description(), category)
        );
    }

    public Template getByMemberAndId(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        validateTemplateAuthorizeMember(member, template);
        return template;
    }

    public List<Template> getByMemberId(Long memberId) {
        return templateRepository.findByMemberId(memberId);
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

    public Template updateTemplate(
            Member member,
            Long templateId,
            UpdateTemplateRequest updateTemplateRequest,
            Category category
            ) {
        Template template = templateRepository.fetchById(templateId);
        validateTemplateAuthorizeMember(member, template);
        template.updateTemplate(updateTemplateRequest.title(), updateTemplateRequest.description(), category);

        return template;
    }

    public void deleteByMemberAndIds(Member member, List<Long> ids) {
        if (ids.size() != new HashSet<>(ids).size()) {
            throw new CodeZapException(HttpStatus.BAD_REQUEST, "삭제하고자 하는 템플릿 ID가 중복되었습니다.");
        }
        for (Long id : ids) {
            deleteById(member, id);
        }
    }

    private void deleteById(Member member, Long id) {
        Template template = templateRepository.fetchById(id);
        validateTemplateAuthorizeMember(member, template);

        templateRepository.deleteById(id);
    }

    private void validateTemplateAuthorizeMember(Member member, Template template) {
        Member owner = template.getMember();
        if (!owner.equals(member)) {
            throw new CodeZapException(HttpStatus.UNAUTHORIZED, "해당 템플릿에 대한 권한이 없습니다.");
        }
    }
}
