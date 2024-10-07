package codezap.auth.provider;

import codezap.member.domain.Member;

public class PlainCredentialProvider implements CredentialProvider {

    private static final String DELIMITER = ";";

    @Override
    public String createCredential(Member member) {
        return String.join(DELIMITER,
                Long.toString(member.getId()),
                member.getName(),
                member.getPassword(),
                member.getSalt());
    }

    @Override
    public Member extractMember(String credential) {
        String[] memberInfo = credential.split(DELIMITER);
        return new Member(
                Long.parseLong(memberInfo[0]),
                memberInfo[1],
                memberInfo[2],
                memberInfo[3]
        );
    }
}
