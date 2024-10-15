package codezap.likes.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import codezap.global.MockMvcTest;

@Import(LikesController.class)
class LikesControllerTest extends MockMvcTest {

    @Test
    @DisplayName("좋아요 생성 성공")
    void likeSuccess() throws Exception {
        long templateId = 1L;

        mvc.perform(post("/like/" + templateId)
                        .param("templateId", String.valueOf(templateId)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("좋아요 삭제 성공")
    void cancelLike() throws Exception {
        long templateId = 1L;

        mvc.perform(delete("/like/" + templateId))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
