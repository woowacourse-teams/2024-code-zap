package codezap.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codezap.auth.dto.LoginMember;
import codezap.auth.dto.request.LoginRequest;
import codezap.global.ServiceTest;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServiceTest extends ServiceTest {

    @Autowired
    private AuthService authService;

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        @Test
        @DisplayName("로그인 성공")
        void login() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            LoginRequest loginRequest = new LoginRequest(member.getName(), MemberFixture.getFixturePlainPassword());

            assertThat(authService.login(loginRequest)).isEqualTo(LoginMember.from(member));
        }

        @Test
        @DisplayName("로그인 실패: 아이디 오류")
        void login_WithInvalidname_ThrowsException() {
            Member member = memberRepository.save(MemberFixture.memberFixture());
            String wrongname = "wrong" + member.getName();

            LoginRequest loginRequest = new LoginRequest(wrongname, member.getPassword());

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("존재하지 않는 아이디 " + wrongname + " 입니다.");
        }

        @Test
        @DisplayName("로그인 실패: 비밀번호 오류")
        void login_WithInvalidPassword_ThrowsException() {
            Member member = memberRepository.save(MemberFixture.memberFixture());

            LoginRequest loginRequest = new LoginRequest(member.getName(), member.getPassword() + "wrong");

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(CodeZapException.class)
                    .hasMessage("로그인에 실패하였습니다. 비밀번호를 확인해주세요.");
        }
    }
}
