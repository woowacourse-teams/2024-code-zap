package codezap.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.template.domain.QSourceCode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SourceCodeQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    @Modifying(clearAutomatically = true)
    public void deleteAllByTemplateIds(List<Long> templateIds) {
        queryFactory.delete(QSourceCode.sourceCode)
                .where(QSourceCode.sourceCode.template.id.in(templateIds))
                .execute();
    }
}
