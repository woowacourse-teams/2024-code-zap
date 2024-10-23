package codezap.likes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.likes.domain.QLikes;
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
