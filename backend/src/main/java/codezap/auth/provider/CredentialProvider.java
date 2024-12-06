package codezap.auth.provider;

import codezap.auth.manager.Credential;
import codezap.member.domain.Member;

public interface CredentialProvider {

    Credential createCredential(Member member);

    Member extractMember(Credential credential);

    String getType();
}
