package codezap.auth.provider;

import codezap.auth.dto.LoginMember;
import codezap.auth.dto.Credential;
import codezap.member.domain.Member;

/**
 * 로그인 정보를 사용해 규격화된 Credential 객체를 생성하는 클래스입니다.
 */
public interface CredentialProvider {

    Credential createCredential(LoginMember loginMember);

    Member extractMember(Credential credential);

    String getType();
}
