package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import codezap.template.domain.SourceCode;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import codezap.template.domain.Visibility;

public class TemplateSpecification implements Specification<Template> {

    private final Long memberId;
    private final String keyword;
    private final Long categoryId;
    private final List<Long> tagIds;
    private final Visibility visibility;

    public TemplateSpecification(
            Long memberId,
            String keyword,
            Long categoryId,
            List<Long> tagIds,
            Visibility visibility
    ) {
        this.memberId = memberId;
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
        this.visibility = visibility;
    }

    @Override
    public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        addMemberPredicate(predicates, criteriaBuilder, root);
        addCategoryPredicate(predicates, criteriaBuilder, root);
        addTagPredicate(predicates, criteriaBuilder, root, query);
        addKeywordPredicate(predicates, criteriaBuilder, root, query);
        addVisibility(predicates, criteriaBuilder, root, query);

        if (query.getResultType().equals(Template.class)) {
            root.fetch("category", JoinType.LEFT);
            root.fetch("member", JoinType.LEFT);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void addMemberPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Template> root) {
        if (memberId != null) {
            predicates.add(criteriaBuilder.equal(root.get("member").get("id"), memberId));
        }
    }

    private void addKeywordPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Template> root,
            CriteriaQuery<?> query
    ) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchKeyword = keyword.trim();

            Predicate titleDescriptionPredicate = criteriaBuilder.isTrue(
                    criteriaBuilder.function("MATCH", Boolean.class,
                            root.get("title"),
                            root.get("description"),
                            criteriaBuilder.literal(searchKeyword)));

            Subquery<Long> sourceCodeSubquery = query.subquery(Long.class);
            Root<SourceCode> sourceCodeRoot = sourceCodeSubquery.from(SourceCode.class);
            sourceCodeSubquery.select(sourceCodeRoot.get("template").get("id"));
            sourceCodeSubquery.where(criteriaBuilder.isTrue(
                    criteriaBuilder.function("MATCH", Boolean.class,
                            sourceCodeRoot.get("content"),
                            sourceCodeRoot.get("filename"),
                            criteriaBuilder.literal(searchKeyword))));

            predicates.add(criteriaBuilder.or(titleDescriptionPredicate, root.get("id").in(sourceCodeSubquery)));
        }
    }

    private void addCategoryPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Template> root
    ) {
        if (categoryId != null) {
            predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
        }
    }

    private void addTagPredicate(
            List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Template> root, CriteriaQuery<?> query
    ) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<TemplateTag> subRoot = subquery.from(TemplateTag.class);
        subquery.select(subRoot.get("template").get("id")).where(subRoot.get("tag").get("id").in(tagIds))
                .groupBy(subRoot.get("template").get("id"))
                .having(criteriaBuilder.equal(criteriaBuilder.countDistinct(subRoot.get("tag").get("id")),
                        tagIds.size()));

        predicates.add(root.get("id").in(subquery));
    }

    private void addVisibility(
            List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Template> root, CriteriaQuery<?> query
    ) {
        if (visibility != null) {
            predicates.add(criteriaBuilder.equal(root.get("visibility"), visibility));
        }
    }
}
