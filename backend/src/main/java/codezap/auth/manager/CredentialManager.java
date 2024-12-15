package codezap.auth.manager;

import codezap.auth.dto.LoginMember;
import codezap.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CredentialManager {

    Member getMember(HttpServletRequest httpServletRequest);

    boolean hasCredential(HttpServletRequest httpServletRequest);

    void setCredential(HttpServletResponse httpServletResponse, LoginMember loginMember);

    void removeCredential(HttpServletResponse httpServletResponse);
}
