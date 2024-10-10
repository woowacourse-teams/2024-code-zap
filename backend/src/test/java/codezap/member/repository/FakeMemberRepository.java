package codezap.member.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;

public class FakeMemberRepository implements MemberRepository {

    private final AtomicLong idCounter = new AtomicLong(1);

    private final List<Member> members;

    public FakeMemberRepository() {
        this.members = new ArrayList<>();
    }

    public FakeMemberRepository(List<Member> members) {
        this.members = new ArrayList<>(members);
        idCounter.set(1 + members.size());
    }

    @Override
    public Member fetchById(Long id) {
        return members.stream()
                .filter(member -> Objects.equals(member.getId(), id))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND,
                        "식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public Member fetchByName(String name) {
        return members.stream()
                .filter(member -> Objects.equals(member.getName(), name))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(ErrorCode.UNAUTHORIZED_ID, "존재하지 않는 아이디 " + name + " 입니다."));
    }

    @Override
    public Member fetchByTemplateId(Long templateId) {
        return null;
    }

    @Override
    public boolean existsByName(String name) {
        return members.stream().anyMatch(member -> Objects.equals(member.getName(), name));
    }

    @Override
    public Member save(Member entity) {
        var saved = new Member(
                getOrGenerateId(entity),
                entity.getName(),
                entity.getPassword(),
                entity.getSalt()
        );
        members.removeIf(member -> Objects.equals(member.getId(), entity.getId()));
        members.add(saved);
        return saved;
    }

    private long getOrGenerateId(Member entity) {
        if (existsById(entity.getId())) {
            return entity.getId();
        }
        return idCounter.getAndIncrement();
    }

    public boolean existsById(Long id) {
        return members.stream().anyMatch(member -> Objects.equals(member.getId(), id));
    }
}
