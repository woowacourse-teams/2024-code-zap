package codezap.template.repository;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;

public class TemplateSpecification implements Specification<Template> {
    private final Long memberId;
    private final String keyword;
    private final Long categoryId;
    private final List<Long> tagIds;

    public TemplateSpecification(Long memberId, String keyword, Long categoryId, List<Long> tagIds) {
        this.memberId = memberId;
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
    }

//    public static Specification<Template> withDynamicQuery(
//            Long memberId, String keyword, Long categoryId, List<Long> tagIds
//    ) {
//        return new TemplateSpecification(memberId, keyword, categoryId, tagIds);
//    }

    @Override
    public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        addMemberPredicate(predicates, criteriaBuilder, root);
        addCategoryPredicate(predicates, criteriaBuilder, root);
        addTagPredicate(predicates, criteriaBuilder, root, query);
        addKeywordPredicate(predicates, criteriaBuilder, root);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void addMemberPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Template> root) {
        if (memberId != null) {
            predicates.add(criteriaBuilder.equal(root.get("member").get("id"), memberId));
        }
    }

    private void addKeywordPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<Template> root) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            String likeKeyword = "%" + keyword.trim() + "%";
            predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get("title"), likeKeyword),
                    criteriaBuilder.like(root.get("description"), likeKeyword)));
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
}
