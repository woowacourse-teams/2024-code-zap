package codezap.auth.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.CheckLoginResponse;
import codezap.auth.dto.response.LoginResponse;
import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "인증 및 인가 API", description = "인증 및 인가 API")
public interface SpringDocAuthController {

    @Operation(summary = "아이디 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
            headers = @Header(name = "Set-Cookie", description = "base64(${name}:${password}); path=\"/\"; HttpOnly;"
                    + " Secure;"))
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/login", errorCases = {
            @ErrorCase(description = "아이디 입력 없음", exampleMessage = "아이디이 입력되지 않았습니다."),
            @ErrorCase(description = "아이디 글자수 오류", exampleMessage = "아이디은 255자 이하로 입력해주세요."),
            @ErrorCase(description = "비밀번호 입력 없음", exampleMessage = "비밀번호가 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 형식 오류", exampleMessage = "영어와 숫자를 포함해야합니다."),
            @ErrorCase(description = "비밀번호 글자수 오류", exampleMessage = "비밀번호는 8~16자 사이로 입력해주세요."),
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/login", errorCases = {
            @ErrorCase(description = "아이디 불일치", exampleMessage = "존재하지 않는 아이디 moly 입니다."),
            @ErrorCase(description = "비밀번호 불일치", exampleMessage = "로그인에 실패하였습니다. 비밀번호를 확인해주세요."),
    })
    ResponseEntity<LoginResponse> login(LoginRequest request, HttpServletResponse response);

    @Operation(summary = "아이디 로그인 후 쿠키 인증")
    @ApiResponse(responseCode = "200", description = "쿠키 인증 성공")
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/login/check", errorCases = {
            @ErrorCase(description = "쿠키 없음", exampleMessage = "쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."),
            @ErrorCase(description = "인증 쿠키 없음", exampleMessage = "인증에 대한 쿠키가 없어서 회원 정보를 찾을 수 없습니다. 다시 로그인해주세요."),
    })
    ResponseEntity<CheckLoginResponse> checkLogin(Member member);

    @Operation(summary = "로그아웃")
    @ApiResponse(responseCode = "204", description = "인증 성공")
    ResponseEntity<Void> logout(HttpServletResponse response);
}
