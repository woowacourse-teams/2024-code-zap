package codezap.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import codezap.auth.dto.request.LoginRequest;
import codezap.global.MvcTest;
import codezap.member.dto.request.SignupRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

class AuthTest extends MvcTest {

    @Nested
    @DisplayName("로그인 테스트")
    class Login {

        @Nested
        @DisplayName("로그인에 성공:")
        class Success {

            private final String name = "name";
            private final String password = "password123!";

            private MvcResult loginResult;

            @BeforeEach
            void successLogin() throws Exception {
                signup(name, password);
                loginResult = requestLogin(name, password);
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

        private MvcResult requestLogin(String name, String password) throws Exception {
            return mvc.perform(post("/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new LoginRequest(name, password))))
                    .andReturn();
        }

        private void signup(String name, String password) throws Exception {
            SignupRequest signupRequest = new SignupRequest(name, password);

            mvc.perform(post("/signup")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signupRequest)));
        }
    }
}