package codezap.member.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codezap.auth.encryption.PasswordEncryptor;
import codezap.auth.encryption.SaltGenerator;
import codezap.category.domain.Category;
import codezap.category.repository.CategoryRepository;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.member.domain.Member;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.response.FindMemberResponse;
import codezap.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final SaltGenerator saltGenerator;
    private final PasswordEncryptor passwordEncryptor;

    @Transactional
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
            throw new CodeZapException(ErrorCode.DUPLICATE_ID, "아이디가 이미 존재합니다.");
        }
    }

    public void existsById(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new CodeZapException(ErrorCode.RESOURCE_NOT_FOUND, "식별자 " + id + "에 해당하는 멤버가 존재하지 않습니다.");
        }
    }

    public FindMemberResponse findMember(Member member, Long id) {
        checkSameMember(member, id);
        return FindMemberResponse.from(member);
    }

    private void checkSameMember(Member member, Long id) {
        if (!Objects.equals(member.getId(), id)) {
            throw new CodeZapException(ErrorCode.FORBIDDEN_ACCESS, "본인의 정보만 조회할 수 있습니다.");
        }
    }

    public Member getByTemplateId(Long templateId) {
        return memberRepository.fetchByTemplateId(templateId);
    }
}
