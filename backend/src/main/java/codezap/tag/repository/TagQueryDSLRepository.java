package codezap.tag.repository;

import static codezap.tag.domain.QTag.tag;
import static codezap.template.domain.QTemplateTag.templateTag;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.tag.domain.QTag;
import codezap.tag.domain.Tag;
import codezap.template.domain.QTemplateTag;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TagQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tag> findMostUsedTagsWithinDateRange(int size, LocalDate startDate) {
        return queryFactory.select(tag)
                .from(templateTag)
                .join(templateTag.tag, tag)
                .where(templateTag.createdAt.goe(startDate.atStartOfDay()))
                .groupBy(tag.id)
                .orderBy(
                        templateTag.count().desc(),
                        templateTag.createdAt.max().desc()
                )
                .limit(size)
                .fetch();
    }

    public List<Tag> findMostUsedTagsByRecentTemplates(int size) {
        return queryFactory.select(tag)
                .from(templateTag)
                .join(templateTag.tag, tag)
                .groupBy(tag.id)
                .orderBy(
                        templateTag.count().desc(),
                        templateTag.createdAt.max().desc()
                )
                .limit(size)
                .fetch();
    }
}
