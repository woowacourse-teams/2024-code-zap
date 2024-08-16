package codezap.member.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;

import codezap.global.exception.CodeZapException;
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
                .orElseThrow(() -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public Member fetchByname(String name) {
        return members.stream()
                .filter(member -> Objects.equals(member.getName(), name))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(HttpStatus.NOT_FOUND, "존재하지 않는 아이디 " + name + " 입니다."));
    }

    @Override
    public boolean existsByname(String name) {
        return members.stream().anyMatch(member -> Objects.equals(member.getName(), name));
    }

    @Override
    public Member save(Member entity) {
        var saved = new Member(
                getOrGenerateId(entity),
                entity.getPassword(),
                entity.getName()
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
