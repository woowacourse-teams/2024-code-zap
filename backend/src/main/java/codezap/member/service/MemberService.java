package codezap.member.service;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import codezap.auth.encryption.PasswordEncryptor;
import codezap.auth.encryption.SaltGenerator;
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
    private final SaltGenerator saltGenerator;
    private final PasswordEncryptor passwordEncryptor;

    public Long signup(SignupRequest request) {
        assertUniqueName(request.name());
        String salt = saltGenerator.generate();
        String encryptedPassword = passwordEncryptor.encrypt(request.password(), salt);
        Member member = memberRepository.save(new Member(request.name(), encryptedPassword, salt));
        categoryRepository.save(Category.createDefaultCategory(member));
        return member.getId();
    }

    public void assertUniqueName(String name) {
        if (memberRepository.existsByName(name)) {
            throw new CodeZapException(HttpStatus.CONFLICT, "아이디가 이미 존재합니다.");
        }
    }

    public FindMemberResponse findMember(MemberDto memberDto, Long id) {
        checkSameMember(memberDto, id);
        return FindMemberResponse.from(memberRepository.fetchById(id));
    }

    public Member getByTemplateId(Long templateId) {
        return memberRepository.fetchByTemplateId(templateId);
    }

    private void checkSameMember(MemberDto memberDto, Long id) {
        if (!Objects.equals(memberDto.id(), id)) {
            throw new CodeZapException(HttpStatus.FORBIDDEN, "본인의 정보만 조회할 수 있습니다.");
        }
    }

    public Member getById(Long id) {
        return memberRepository.fetchById(id);
    }
}
