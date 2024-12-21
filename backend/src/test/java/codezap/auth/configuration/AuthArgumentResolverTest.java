package codezap.auth.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codezap.auth.dto.LoginMember;
import codezap.auth.dto.Credential;
import codezap.auth.manager.AuthorizationHeaderCredentialManager;
import java.lang.reflect.Method;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.ReflectionSupport;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.StandardServletAsyncWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import codezap.auth.manager.CookieCredentialManager;
import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.auth.provider.PlainCredentialProvider;
import codezap.fixture.MemberFixture;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;

class AuthArgumentResolverTest {
    private final CredentialProvider credentialProvider = new PlainCredentialProvider();
    private final List<CredentialManager> credentialManagers =
            List.of(new CookieCredentialManager(), new AuthorizationHeaderCredentialManager());

    private final AuthArgumentResolver authArgumentResolver = new AuthArgumentResolver(credentialManagers, credentialProvider);

    @Nested
    @DisplayName("지원하는 파라미터 테스트")
    class SupportsParameterTest {

        static class SupportTestController {
            public void supportMethod(@AuthenticationPrinciple Member member) {
            }

            public void notSupportMethod(Member member) {
            }
        }

        @Test
        @DisplayName("성공: AuthenticationPrinciple 어노테이션이 파라미터에 존재하면 지원할 수 있는 메서드이다.")
        void canSupportsTest() {
            Method supportMethod = ReflectionSupport.findMethod(
                            SupportTestController.class, "supportMethod", Member.class)
                    .orElseThrow();
            MethodParameter methodParameter = new MethodParameter(supportMethod, 0);

            assertThat(authArgumentResolver.supportsParameter(methodParameter))
                    .isTrue();
        }

        @Test
        @DisplayName("성공: AuthenticationPrinciple 어노테이션이 파라미터에 존재하지 않으면 지원할 수 없는 메서드이다.")
        void notSupportsTest() {
            Method supportMethod = ReflectionSupport.findMethod(
                            SupportTestController.class, "notSupportMethod", Member.class)
                    .orElseThrow();
            MethodParameter methodParameter = new MethodParameter(supportMethod, 0);

            assertThat(authArgumentResolver.supportsParameter(methodParameter))
                    .isFalse();
        }
    }

    @Nested
    @DisplayName("파라미터 반환 테스트")
    class ResolveArgument {

        static class ResolveTestController {
            public void notRequiredMethod(@AuthenticationPrinciple(required = false) Member member) {
            }

            public void requiredMethod(@AuthenticationPrinciple Member member) {
            }
        }

        private final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest("GET", "/templates");
        private final StandardServletAsyncWebRequest nativeWebRequest =
                new StandardServletAsyncWebRequest(httpServletRequest, new MockHttpServletResponse());
        private final Member member = MemberFixture.getFirstMember();

        @Nested
        @DisplayName("required 값이 false 일 경우")
        class RequiredFalseTest {
            Method notRequiredMethod = ReflectionSupport.findMethod(ResolveTestController.class,
                            "notRequiredMethod", Member.class)
                    .orElseThrow();

            @Test
            @DisplayName("성공: credential 정보가 없을 때 null 이 반환된다.")
            void noCredentialTest() {
                //when
                Member member = resolveArgument(notRequiredMethod, nativeWebRequest);

                //then
                assertThat(member).isNull();
            }

            @Test
            @DisplayName("성공: credential 정보가 존재하면 Member 가 반환된다.")
            void existCredentialTest() {
                //given
                setCredentialCookie(httpServletRequest, member);

                //when
                Member resolvedArgument = resolveArgument(notRequiredMethod, nativeWebRequest);

                //then
                assertThat(resolvedArgument).isEqualTo(member);
            }
        }

        @Nested
        @DisplayName("required 값이 true  일 경우")
        class RequiredTrueTest {
            Method requiredMethod = ReflectionSupport.findMethod(ResolveTestController.class,
                            "requiredMethod", Member.class)
                    .orElseThrow();

            @Test
            @DisplayName("실패: credential 정보가 없을때 예외가 발생한다.")
            void noCredentialTest() {
                //given

                //when & then
                assertThatThrownBy(() -> resolveArgument(requiredMethod, nativeWebRequest))
                        .isInstanceOf(CodeZapException.class)
                        .hasMessage("인증 정보가 없습니다. 다시 로그인해 주세요.");
            }

            @Test
            @DisplayName("성공: credential 정보가 존재하면 Member 가 반환된다.")
            void existCredentialTest() {
                //given
                setCredentialCookie(httpServletRequest, member);

                //when
                Member resolvedArgument = resolveArgument(requiredMethod, nativeWebRequest);

                //then
                assertThat(resolvedArgument).isEqualTo(member);
            }
        }

        private Member resolveArgument(Method method, NativeWebRequest webRequest) {
            return authArgumentResolver.resolveArgument(
                    new MethodParameter(method, 0),
                    new ModelAndViewContainer(),
                    webRequest,
                    (request, target, objectName) -> null);
        }

        private void setCredentialCookie(MockHttpServletRequest request, Member member) {
            MockHttpServletResponse mockResponse = new MockHttpServletResponse();
            Credential credential = credentialProvider.createCredential(LoginMember.from(member));
            credentialManagers.forEach(
                    credentialManager -> credentialManager.setCredential(mockResponse, credential));
            request.setCookies(mockResponse.getCookies());
        }
    }
}
