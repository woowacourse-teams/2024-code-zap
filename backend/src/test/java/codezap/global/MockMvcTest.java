package codezap.global;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.category.service.CategoryService;
import codezap.global.cors.CorsProperties;
import codezap.likes.service.LikesService;
import codezap.member.fixture.MemberFixture;
import codezap.member.service.MemberService;
import codezap.tag.service.TagService;

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
    protected CategoryService categoryService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected TagService tagService;

    @MockBean
    protected LikesService likesService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        objectMapper = new ObjectMapper();

        when(credentialManager.getCredential(any())).thenReturn("mock-credential");
        when(credentialProvider.extractMember(anyString())).thenReturn(MemberFixture.memberFixture());
    }
}
