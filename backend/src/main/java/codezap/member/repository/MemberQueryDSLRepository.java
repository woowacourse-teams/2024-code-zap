package codezap.member.repository;

import static codezap.template.domain.QTemplate.template;

import java.util.Optional;

import org.springframework.stereotype.Repository;


import com.querydsl.jpa.impl.JPAQueryFactory;

import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberQueryDSLRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Member> findByTemplateId(Long templateId) {
        return Optional.ofNullable(queryFactory.select(template.member)
                .from(template)
                .where(template.id.eq(templateId))
                .fetchOne());
    }
}
