package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
import codezap.template.domain.Template;

public class FakeTemplateRepository implements TemplateRepository {

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<Template> templates;

    public FakeTemplateRepository() {
        this.templates = new ArrayList<>();
    }

    @Override
    public Template fetchById(Long id) {
        return templates.stream()
                .filter(template -> Objects.equals(template.getId(), id))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 템플릿이 존재하지 않습니다."));
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return templates.stream().anyMatch(template -> Objects.equals(template.getCategory().getId(), categoryId));
    }

    public Page<Template> searchBy(Long memberId, String keyword, Pageable pageable) {
        List<Template> searchedTemplates = templates.stream()
                .filter(template -> Objects.equals(template.getMember().getId(), memberId))
                .filter(template -> containTopic(keyword, template))
                .toList();

        return pageTemplates(pageable, searchedTemplates);
    }

    public Page<Template> searchBy(Long memberId, String keyword, List<Long> templateIds, Pageable pageable) {
        List<Template> searchedTemplates = templates.stream()
                .filter(template -> Objects.equals(template.getMember().getId(), memberId))
                .filter(template -> templateIds.contains(template.getId()))
                .filter(template -> containTopic(keyword, template))
                .toList();

        return pageTemplates(pageable, searchedTemplates);
    }

    public Page<Template> searchBy(Long memberId, String keyword, Long categoryId, Pageable pageable) {
        List<Template> searchedTemplates = templates.stream()
                .filter(template -> Objects.equals(template.getMember().getId(), memberId))
                .filter(template -> Objects.equals(template.getCategory().getId(), categoryId))
                .filter(template -> containTopic(keyword, template))
                .toList();

        return pageTemplates(pageable, searchedTemplates);
    }

    public Page<Template> searchBy(Long memberId, String keyword, Long categoryId, List<Long> templateIds,
            Pageable pageable
    ) {
        List<Template> searchedTemplates = templates.stream()
                .filter(template -> Objects.equals(template.getMember().getId(), memberId))
                .filter(template -> Objects.equals(template.getCategory().getId(), categoryId))
                .filter(template -> templateIds.contains(template.getId()))
                .filter(template -> containTopic(keyword, template))
                .toList();

        return pageTemplates(pageable, searchedTemplates);
    }

    private static PageImpl<Template> pageTemplates(Pageable pageable, List<Template> templates) {
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), templates.size());
        List<Template> pagedTemplates = templates.subList(start, end);

        return new PageImpl<>(pagedTemplates, pageable, templates.size());
    }

    private static boolean containTopic(String topic, Template template) {
        String keyword = topic.substring(1, topic.length() - 1);
        return template.getTitle().contains(keyword) ||
                template.getSourceCodes().stream().anyMatch(sourceCode -> sourceCode.getFilename().contains(keyword)) ||
                template.getSourceCodes().stream().anyMatch(sourceCode -> sourceCode.getContent().contains(keyword)) ||
                template.getDescription().contains(keyword);
    }

    @Override
    public List<Template> findAll() {
        return templates;
    }

    @Override
    public List<Template> findByMemberId(Long id) {
        return templates.stream()
                .filter(template -> Objects.equals(template.getMember().getId(), id))
                .toList();
    }

    @Override
    public Page<Template> findAll(Specification<Template> specification, Pageable pageable) {
        return null;
    }

    @Override
    public Template save(Template entity) {
        var saved = new Template(
                getOrGenerateId(entity),
                entity.getMember(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getSourceCodes()
        );
        templates.removeIf(template -> Objects.equals(template.getId(), entity.getId()));
        templates.add(saved);
        return saved;
    }

    @Override
    public void deleteById(Long id) {
        templates.removeIf(template -> Objects.equals(template.getId(), id));
    }

    private long getOrGenerateId(Template entity) {
        if (existsById(entity.getId())) {
            return entity.getId();
        }
        return idCounter.getAndIncrement();
    }

    public boolean existsById(Long id) {
        return templates.stream().anyMatch(template -> Objects.equals(template.getId(), id));
    }
}
