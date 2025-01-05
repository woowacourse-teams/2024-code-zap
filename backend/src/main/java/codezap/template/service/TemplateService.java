package codezap.template.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.category.domain.Category;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.global.pagination.FixedPage;
import codezap.member.domain.Member;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import codezap.template.dto.request.CreateTemplateRequest;
import codezap.template.dto.request.UpdateTemplateRequest;
import codezap.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemplateService {

    private final TemplateRepository templateRepository;

    @Transactional
    public Template create(Member member, CreateTemplateRequest request, Category category) {
        Template template = new Template(
                member,
                request.title(),
                request.description(),
                category,
                request.visibility());
        return templateRepository.save(template);
    }

    public Template getById(Long id) {
        return templateRepository.fetchById(id);
    }

    public FixedPage<Template> findAllBy(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility,
            Pageable pageable
    ) {
        return templateRepository.findAll(memberId, keyword, categoryId, tagIds, visibility, pageable);
    }

    public FixedPage<Template> findAllByMemberId(Long memberId, Pageable pageable) {
        return templateRepository.findAllLikedByMemberId(memberId, pageable);
    }

    @Transactional
    public Template update(
            Member member,
            Long templateId,
            UpdateTemplateRequest request,
            Category category
    ) {
        Template template = templateRepository.fetchById(templateId);
        if (!template.matchMember(member)) {
            throw new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "해당 템플릿에 대한 권한이 없습니다.");
        }
        template.updateTemplate(
                request.title(),
                request.description(),
                category,
                request.visibility()
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
        if (!template.matchMember(member)) {
            throw new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "해당 템플릿에 대한 권한이 없습니다.");
        }
        templateRepository.deleteById(id);
    }
}
