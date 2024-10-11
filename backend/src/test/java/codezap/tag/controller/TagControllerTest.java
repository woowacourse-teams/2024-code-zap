package codezap.tag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import codezap.global.MockMvcTest;
import codezap.tag.domain.Tag;
import codezap.tag.dto.response.FindAllTagsResponse;
import codezap.tag.dto.response.FindTagResponse;

@Import(TagController.class)
class TagControllerTest extends MockMvcTest {

    @Test
    @DisplayName("모든 태그 조회 성공")
    void getTags() throws Exception {
        // given
        FindAllTagsResponse findAllTagsResponse = new FindAllTagsResponse(List.of(
                FindTagResponse.from(new Tag(1L, "tag1")),
                FindTagResponse.from(new Tag(2L, "tag2"))
        ));

        when(tagService.findAllByMemberId(any())).thenReturn(findAllTagsResponse);

        // when & then
        mvc.perform(get("/tags?memberId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags.size()").value(2));
    }
}
