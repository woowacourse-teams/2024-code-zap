package codezap.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.member.domain.Member;
import codezap.member.dto.request.SignupRequest;
import codezap.member.dto.response.FindMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원 API", description = "회원 API")
public interface SpringDocMemberController {

    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "201", description = "회원가입 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/members", errorCases = {
            @ErrorCase(description = "아이디 입력 없음", exampleMessage = "아이디이 입력되지 않았습니다."),
            @ErrorCase(description = "아이디 글자수 오류", exampleMessage = "아이디은 255자 이하로 입력해주세요."),
            @ErrorCase(description = "비밀번호 입력 없음", exampleMessage = "비밀번호가 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 형식 오류", exampleMessage = "영어와 숫자를 포함해야합니다."),
            @ErrorCase(description = "비밀번호 글자수 오류", exampleMessage = "비밀번호는 8~16자 사이로 입력해주세요."),
    })
    @ApiErrorResponse(status = HttpStatus.CONFLICT, instance = "/signup", errorCases = {
            @ErrorCase(description = "아이디 중복", exampleMessage = "아이디가 이미 존재합니다."),
    })
    ResponseEntity<Void> signup(@RequestBody SignupRequest request);

    @Operation(summary = "사용자명 중복 확인")
    @ApiResponse(responseCode = "200", description = "사용가능한 아이디")
    @ApiErrorResponse(status = HttpStatus.CONFLICT, instance = "/check-login-id", errorCases = {
            @ErrorCase(description = "아이디 중복", exampleMessage = "아이디가 이미 존재합니다."),
    })
    void checkUniquename(@RequestParam String name);

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "회원 정보 조회", description = "회원의 정보(아이디일, 닉네임)을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공")
    @ApiErrorResponse(status = HttpStatus.FORBIDDEN, instance = "/members/1", errorCases = {
            @ErrorCase(description = "로그인된 회원이 조회하려는 회원이 아닌 경우", exampleMessage = "본인의 정보만 조회할 수 있습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND, instance = "/members/1", errorCases = {
            @ErrorCase(description = "조회하려는 id 값인 회원이 없는 경우", exampleMessage = "식별자 1에 해당하는 회원이 존재하지 않습니다.")
    })
    ResponseEntity<FindMemberResponse> findMember(Member member, Long id);
}
