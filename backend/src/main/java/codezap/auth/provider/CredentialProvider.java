package codezap.auth.provider;

import codezap.member.domain.Member;

public interface CredentialProvider {

    String createCredential(Member member);

    Member extractMember(String credential);

    String getType();
}
