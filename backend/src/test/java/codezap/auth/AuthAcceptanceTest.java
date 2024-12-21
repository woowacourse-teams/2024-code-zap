package codezap.auth;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import codezap.auth.dto.request.LoginRequest;
import codezap.global.MvcTest;
import codezap.member.dto.request.SignupRequest;
import jakarta.servlet.http.Cookie;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

class AuthAcceptanceTest extends MvcTest {

    @Nested
    @DisplayName("로그인 테스트")
    class Login {

        @Nested
        @DisplayName("성공: 올바른 아이디와 비밀번호를 사용하여 로그인에 성공")
        class Success {

            private final String name = "name";
            private final String password = "password123!";

            private MvcResult loginResult;

            @BeforeEach
            void successLogin() throws Exception {
                signup(name, password);
                loginResult = requestLogin(name, password).andReturn();
            }

            @Test
            @DisplayName("정상적인 인증 쿠키 반환")
            void responseCookie() throws Exception {
                //when
                Cookie[] cookies = loginResult.getResponse().getCookies();

                //then
                mvc.perform(get("/login/check")
                                .cookie(cookies))
                        .andExpect(status().isOk());
            }

            @Test
            @DisplayName("정상적인 인증 헤더 반환")
            void responseHeader() throws Exception {
                //when & then
                MockHttpServletResponse response = loginResult.getResponse();
                String authorizationHeader = response.getHeader(HttpHeaders.AUTHORIZATION);

                mvc.perform(get("/login/check")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("로그인 실패:")
        class FailLogin {

            @Test
            @DisplayName("회원가입 하지 않은 정보로 로그인 시도")
            void noSignup() throws Exception {
                String notExistName = "noSignup";
                requestLogin(notExistName, "password123!")
                        .andExpect(status().isUnauthorized())
                        .andExpect(jsonPath("$.detail").value("존재하지 않는 아이디 %s 입니다.".formatted(notExistName)));
            }

            @Test
            @DisplayName("잘못된 비밀번호로 로그인 시도")
            void wrongPassword() throws Exception {
                String name = "name";
                String password = "password123!";
                signup(name, password);

                requestLogin(name, "wrongPassword123!")
                        .andExpect(status().isUnauthorized())
                        .andExpect(jsonPath("$.detail").value("로그인에 실패하였습니다. 비밀번호를 확인해주세요."));
            }
        }
    }

    @Nested
    @DisplayName("로그인 확인")
    class CheckLogin {

        @Test
        @DisplayName("실패: 인증 정보 없음")
        void noCredential() throws Exception {
            mvc.perform(get("/login/check"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("인증 정보가 없습니다. 다시 로그인해 주세요."));
        }
    }

    @Nested
    @DisplayName("로그아웃 성공:")
    class Logout {

        @Test
        @DisplayName("쿠키 인증 정보를 정상적으로 삭제")
        void logoutWithCookie() throws Exception {
            //given
            String name = "name";
            String password = "password123!";
            signup(name, password);
            MvcResult loginResponse = requestLogin(name, password).andReturn();
            Pattern expireCookieRegex = Pattern.compile("credential=.*?; Max-Age=0;.*?");

            //when
            mvc.perform(post("/logout")
                            .cookie(loginResponse.getResponse().getCookies()))
                    .andExpect(header().string(HttpHeaders.SET_COOKIE, matchesPattern(expireCookieRegex)))
                    .andReturn();
        }

        @Disabled
        @Test
        @DisplayName("Authorization 헤더의 로그아웃은 클라이언트에서 구현을 한다.")
        void loginWithAuthorizationHeader() {
        }
    }

    private void signup(String name, String password) throws Exception {
        SignupRequest signupRequest = new SignupRequest(name, password);

        mvc.perform(post("/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));
    }

    private ResultActions requestLogin(String name, String password) throws Exception {
        return mvc.perform(post("/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest(name, password))));
    }
}
