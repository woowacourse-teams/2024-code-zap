package codezap.voc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import codezap.auth.manager.CredentialManager;
import codezap.auth.provider.CredentialProvider;
import codezap.global.cors.CorsProperties;
import codezap.voc.dto.VocRequest;
import codezap.voc.service.VocService;

@WebMvcTest(VocController.class)
@EnableConfigurationProperties(CorsProperties.class)
class VocControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VocService vocService;

    @MockBean
    private CredentialProvider credentialProvider;

    @MockBean
    private CredentialManager credentialManager;

    @Test
    void create() throws Exception {
        // given
        doNothing().when(vocService).create(any(VocRequest.class));

        var request = new VocRequest("lorem ipsum dolor sit amet consectetur adipiscing elit", null);

        // when & then
        mvc.perform(post("/contact")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
