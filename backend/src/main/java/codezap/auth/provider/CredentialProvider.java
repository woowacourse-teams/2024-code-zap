package codezap.auth.provider;

import codezap.auth.dto.LoginMember;
import codezap.auth.manager.Credential;
import codezap.member.domain.Member;

public interface CredentialProvider {

    Credential createCredential(LoginMember loginMember);

    Member extractMember(Credential credential);

    String getType();
}
