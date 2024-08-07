package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Template> searchByTopic(Long memberId, String topic, Pageable pageable) {
        List<Template> searchedTemplates = templates.stream()
                .filter(template -> Objects.equals(template.getMember().getId(), memberId))
                .filter(template -> containTopic(topic, template))
                .toList();

        return pageTemplates(pageable, searchedTemplates);
    }

    private static PageImpl<Template> pageTemplates(Pageable pageable, List<Template> templates) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), templates.size());
        List<Template> pagedTemplates = templates.subList(start, end);

        return new PageImpl<>(pagedTemplates, pageable, templates.size());
    }

    private static boolean containTopic(String topic, Template template) {
        return template.getTitle().contains(topic) ||
                template.getSnippets().stream().anyMatch(snippet -> snippet.getFilename().contains(topic)) ||
                template.getSnippets().stream().anyMatch(snippet -> snippet.getContent().contains(topic)) ||
                template.getDescription().contains(topic);
    }

    @Override
    public Page<Template> findBy(Pageable pageable) {
        return pageTemplates(pageable, templates);
    }

    @Override
    public Page<Template> findByCategoryId(Pageable pageable, Long categoryId) {
        return pageTemplates(
                pageable,
                templates.stream()
                        .filter(template -> Objects.equals(template.getCategory().getId(), categoryId))
                        .toList()
        );
    }

    @Override
    public Page<Template> findByIdIn(PageRequest pageRequest, List<Long> templateIds) {
        return null;
    }

    @Override
    public Page<Template> findByIdInAndCategoryId(PageRequest pageRequest, List<Long> templateIds, Long categoryId) {
        return null;
    }

    @Override
    public List<Template> findAll() {
        return templates;
    }

    @Override
    public long countByCategoryId(Long categoryId) {
        return templates.stream().filter(template -> Objects.equals(template.getCategory().getId(), categoryId))
                .count();
    }

    @Override
    public long countByIdInAndCategoryId(List<Long> templateIds, Long categoryId) {
        return 0;
    }

    @Override
    public long countByIdIn(List<Long> templateIds) {
        return 0;
    }

    @Override
    public long count() {
        return templates.size();
    }

    @Override
    public Template save(Template entity) {
        var saved = new Template(
                getOrGenerateId(entity),
                entity.getMember(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getSnippets()
        );
        templates.removeIf(template -> Objects.equals(template.getId(), entity.getId()));
        templates.add(saved);
        return saved;
    }

    @Override
    public List<Template> saveAll(List<Template> templates) {
        return templates.stream().map(this::save).toList();
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
