package codezap.template.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.template.domain.QThumbnail;
import codezap.template.domain.Template;
import codezap.template.domain.Thumbnail;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ThumbnailQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Thumbnail> findByTemplate(Template template) {
        return Optional.ofNullable(queryFactory.selectFrom(QThumbnail.thumbnail)
                .innerJoin(QThumbnail.thumbnail.sourceCode).fetchJoin()
                .where(QThumbnail.thumbnail.template.eq(template))
                .fetchOne());
    }

    @Modifying(clearAutomatically = true)
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        queryFactory.delete(QThumbnail.thumbnail)
                .where(QThumbnail.thumbnail.template.id.in(templateIds))
                .execute();
    }

    public List<Thumbnail> findAllByTemplateIn(List<Long> templateIds) {
        return queryFactory.selectFrom(QThumbnail.thumbnail)
                .innerJoin(QThumbnail.thumbnail.sourceCode).fetchJoin()
                .where(QThumbnail.thumbnail.template.id.in(templateIds))
                .fetch();
    }
}
