package codezap.likes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.likes.domain.Likes;
import codezap.likes.domain.QLikes;
import codezap.template.domain.QTemplate;
import codezap.template.domain.Template;
import codezap.template.domain.Visibility;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class LikesQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Modifying(clearAutomatically = true)
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        jpaQueryFactory.delete(QLikes.likes)
            .where(QLikes.likes.template.id.in(templateIds))
            .execute();
    }
}
