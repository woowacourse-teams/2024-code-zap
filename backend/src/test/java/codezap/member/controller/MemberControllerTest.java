package codezap.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import codezap.fixture.MemberFixture;
import codezap.global.MockMvcTest;
import codezap.member.domain.Member;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.response.FindMemberResponse;

@Import(MemberController.class)
class MemberControllerTest extends MockMvcTest {

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() throws Exception {
        SignupRequest signupRequest = new SignupRequest("testuser", "password123");

        mvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("사용자명 중복 확인 성공")
    void checkUniqueNameSuccess() throws Exception {
        String name = "name";

        doNothing().when(memberService).assertUniqueName(any(String.class));

        mvc.perform(get("/check-name")
                        .param("name", name))
                .andDo(print())
                .andExpect(status().isOk());
        verify(memberService, times(1)).assertUniqueName(name);
    }


    @Test
    @DisplayName("회원 조회 성공")
    void findMemberNameSuccess() throws Exception {
        Member member = MemberFixture.getFirstMember();
        FindMemberResponse response = FindMemberResponse.from(member);

        when(memberService.findMember(anyLong())).thenReturn(response);

        mvc.perform(get("/members/" + member.getId() + "/name"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(member.getName()));
    }
}
