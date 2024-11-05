package codezap.member.repository;

import org.springframework.stereotype.Repository;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryDSLRepository memberQueryDSLRepository;

    public Member fetchById(Long id) {
        return memberJpaRepository.findById(id).orElseThrow(
                () -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    public Member fetchByName(String name) {
        return memberJpaRepository.findByName(name)
                .orElseThrow(() -> new CodeZapException(ErrorCode.UNAUTHORIZED_ID, "존재하지 않는 아이디 " + name + " 입니다."));
    }

    public Member fetchByTemplateId(Long templateId) {
        return memberQueryDSLRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "템플릿에 대한 멤버가 존재하지 않습니다."));
    }

    public boolean existsByName(String name) {
        return memberJpaRepository.existsByName(name);
    }

    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }
}
