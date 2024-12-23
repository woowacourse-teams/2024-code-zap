package codezap.voc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import codezap.global.MockMvcTest;
import codezap.voc.dto.VocRequest;

@Import(VocController.class)
class VocControllerTest extends MockMvcTest {

    @Test
    @DisplayName("문의하기 요청 성공")
    void create() throws Exception {
        var request = new VocRequest("lorem ipsum dolor sit amet consectetur adipiscing elit", null);

        mvc.perform(post("/contact")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("문의하기 요청 실패 : 요청 본문 없음")
    void create_error() throws Exception {
        mvc.perform(post("/contact"))
                .andExpect(status().isBadRequest());
    }
}
