package codezap.global;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.category.service.facade.MemberCategoryApplicationService;
import codezap.category.service.facade.MemberCategoryTemplateApplicationService;
import codezap.member.fixture.MemberFixture;
import codezap.member.service.MemberService;
import codezap.template.service.TemplateService;

@WebMvcTest(SpringExtension.class)
public abstract class MockMvcTest {

    @Autowired
    protected MockMvc mvc;

    protected ObjectMapper objectMapper;

    @MockBean
    protected CredentialProvider credentialProvider;

    @MockBean
    protected CredentialManager credentialManager;

    @MockBean
    protected TemplateService templateService;

    @MockBean
    protected MemberCategoryApplicationService memberCategoryApplicationService;

    @MockBean
    protected MemberCategoryTemplateApplicationService memberCategoryTemplateApplicationService;

    @MockBean
    protected MemberService memberService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        objectMapper = new ObjectMapper();

        when(credentialManager.getCredential(any())).thenReturn("mock-credential");
        when(credentialProvider.extractMember(anyString())).thenReturn(MemberFixture.memberFixture());
    }
}
