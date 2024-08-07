package codezap.member.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        this.members = members;
    }

    @Override
    public Member fetchById(Long id) {
        return members.stream()
                .filter(member -> Objects.equals(member.getId(), id))
                .findFirst()
                .orElseThrow(() -> new CodeZapException(HttpStatus.NOT_FOUND, "식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다."));
    }

    @Override
    public boolean existsByEmail(String email) {
        return members.stream().anyMatch(member -> Objects.equals(member.getEmail(), email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return members.stream().anyMatch(member -> Objects.equals(member.getUsername(), username));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return members.stream()
                .filter(member -> Objects.equals(member.getEmail(), email))
                .findFirst();
    }

    @Override
    public Member save(Member entity) {
        var saved = new Member(
                getOrGenerateId(entity),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUsername()
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
