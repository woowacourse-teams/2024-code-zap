package codezap.member.service;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.response.FindMemberResponse;
import codezap.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public Member signup(SignupRequest request) {
        assertUniqueEmail(request.email());
        assertUniqueUsername(request.username());
        Member member = new Member(request.email(), request.password(), request.username());
        Member saved = memberRepository.save(member);
        categoryRepository.save(Category.createDefaultCategory(saved));
        return saved;
    }

    public void assertUniqueEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "이메일이 이미 존재합니다.");
        }
    }

    public void assertUniqueUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "사용자명이 이미 존재합니다.");
        }
    }

    public FindMemberResponse findMember(MemberDto memberDto, Long id) {
        checkSameMember(memberDto, id);
        return FindMemberResponse.from(memberRepository.fetchById(id));
    }

    private void checkSameMember(MemberDto memberDto, Long id) {
        if (!Objects.equals(memberDto.id(), id)) {
            throw new CodeZapException(HttpStatus.FORBIDDEN, "본인의 정보만 조회할 수 있습니다.");
        }
    }
}
