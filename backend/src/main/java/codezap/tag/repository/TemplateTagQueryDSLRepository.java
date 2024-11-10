package codezap.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.tag.domain.QTag;
import codezap.tag.domain.Tag;
import codezap.template.domain.QTemplate;
import codezap.template.domain.QTemplateTag;
import codezap.template.domain.Template;
import codezap.template.domain.TemplateTag;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TemplateTagQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tag> findAllTagsByTemplate(Template template) {
        return queryFactory.selectFrom(QTag.tag)
                .innerJoin(QTemplateTag.templateTag)
                .on(QTag.tag.id.eq(QTemplateTag.templateTag.id.tagId))
                .where(QTemplateTag.templateTag.template.eq(template))
                .fetch();
    }

    public List<TemplateTag> findAllByTemplateId(Long templateId) {
        return queryFactory.selectFrom(QTemplateTag.templateTag)
                .innerJoin(QTemplateTag.templateTag.tag).fetchJoin()
                .where(QTemplateTag.templateTag.id.templateId.eq(templateId))
                .fetch();
    }

    public List<TemplateTag> findAllByTemplateIdsIn(List<Long> templateIds) {
        return queryFactory.selectFrom(QTemplateTag.templateTag)
                .innerJoin(QTemplateTag.templateTag.tag).fetchJoin()
                .where(QTemplateTag.templateTag.id.templateId.in(templateIds))
                .fetch();
    }

    public List<Tag> findAllTagDistinctByMemberId(Long memberId) {
        QTag tag = QTag.tag;
        QTemplateTag templateTag = QTemplateTag.templateTag;
        QTemplate template = QTemplate.template;

        return queryFactory.selectDistinct(tag)
                .from(tag)
                .join(templateTag).on(tag.id.eq(templateTag.id.tagId))
                .join(template).on(templateTag.id.templateId.eq(template.id))
                .where(template.member.id.eq(memberId))
                .fetch();
    }

    @Modifying(clearAutomatically = true)
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        queryFactory.delete(QTemplateTag.templateTag)
                .where(QTemplateTag.templateTag.id.templateId.in(templateIds))
                .execute();
    }
}
