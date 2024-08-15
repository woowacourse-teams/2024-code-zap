package codezap.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import codezap.global.MockMvcTest;
import codezap.global.exception.CodeZapException;
import codezap.member.domain.Member;
import codezap.member.dto.MemberDto;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.response.FindMemberResponse;
import codezap.member.fixture.MemberFixture;

@Import(MemberController.class)
class MemberControllerTest extends MockMvcTest {

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() throws Exception {
        SignupRequest signupRequest = new SignupRequest("test@email.com", "password123", "testuser");

        mvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("이메일 중복 확인 성공")
    void checkUniqueEmailSuccess() throws Exception {
        doNothing().when(memberService).assertUniqueEmail(any(String.class));

        mvc.perform(get("/check-email")
                        .param("email", "unique@email.com"))
                .andExpect(status().isOk());
        verify(memberService, times(1)).assertUniqueEmail("unique@email.com");
    }

    @Test
    @DisplayName("사용자명 중복 확인 성공")
    void checkUniqueUsernameSuccess() throws Exception {
        doNothing().when(memberService).assertUniqueUsername(any(String.class));

        mvc.perform(get("/check-username")
                        .param("username", "uniqueuser"))
                .andExpect(status().isOk());
        verify(memberService, times(1)).assertUniqueUsername("uniqueuser");
    }


    @Test
    @DisplayName("회원 조회 성공")
    void findMemberSuccess() throws Exception {
        Member member = MemberFixture.memberFixture();
        FindMemberResponse response = FindMemberResponse.from(member);

        when(memberService.findMember(any(), any())).thenReturn(response);

        mvc.perform(get("/members/" + member.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(member.getUsername()))
                .andExpect(jsonPath("$.email").value(member.getEmail()));
    }
}
