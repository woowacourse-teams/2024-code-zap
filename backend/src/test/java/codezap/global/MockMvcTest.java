package codezap.global;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.auth.dto.Credential;
import codezap.auth.manager.CredentialManager;
import codezap.auth.manager.CredentialManagers;
import codezap.auth.provider.CredentialProvider;
import codezap.category.service.CategoryService;
import codezap.fixture.MemberFixture;
import codezap.global.cors.CorsProperties;
import codezap.global.exception.CodeZapException;
import codezap.global.exception.ErrorCode;
import codezap.likes.service.LikesService;
import codezap.member.domain.Member;
import codezap.member.service.MemberService;
import codezap.tag.service.TagService;
import codezap.template.service.facade.TemplateApplicationService;
import codezap.voc.service.VocService;

@WebMvcTest(SpringExtension.class)
@EnableConfigurationProperties(CorsProperties.class)
public abstract class MockMvcTest {

    protected MockMvc mvc;

    protected ObjectMapper objectMapper;

    @MockBean
    protected CredentialProvider credentialProvider;

    @MockBean
    protected CredentialManager credentialManager;

    @MockBean
    protected CredentialManagers credentialManagers;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected TagService tagService;

    @MockBean
    protected LikesService likesService;

    @MockBean
    private VocService vocService;

    @MockBean
    protected TemplateApplicationService templateApplicationService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        objectMapper = new ObjectMapper();

        when(credentialManager.hasCredential(any())).thenReturn(true);
        Member member = MemberFixture.getFirstMember();
        when(credentialProvider.extractMember(any())).thenReturn(member);
    }

    protected void setLogin() {
        when(credentialManagers.hasCredential(any(HttpServletRequest.class))).thenReturn(true);
        when(credentialProvider.extractMember(any(Credential.class))).thenReturn(MemberFixture.getFirstMember());
    }

    protected void setNoLogin() {
        when(credentialManagers.hasCredential(any(HttpServletRequest.class))).thenReturn(false);
        doThrow(new CodeZapException(ErrorCode.UNAUTHORIZED_USER, "인증 정보가 없습니다. 다시 로그인해 주세요.")).when(
                credentialManagers).getCredential(any(HttpServletRequest.class));
    }
}
