package codezap.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.auth.dto.request.LoginRequest;
import codezap.auth.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "인증 및 인가 API", description = "인증 및 인가 API")
public interface SpringDocAuthController {

    @Operation(summary = "이메일 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
            headers = @Header(name = "Set-Cookie", description = "base64(${loginId}:${password}); path=\"/\"; HttpOnly;"
                    + " Secure;"))
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/login", errorCases = {
            @ErrorCase(description = "이메일 입력 없음", exampleMessage = "이메일이 입력되지 않았습니다."),
            @ErrorCase(description = "이메일 형식 오류", exampleMessage = "이메일 형식이 아닙니다."),
            @ErrorCase(description = "이메일 글자수 오류", exampleMessage = "이메일은 255자 이하로 입력해주세요."),
            @ErrorCase(description = "비밀번호 입력 없음", exampleMessage = "비밀번호가 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 형식 오류", exampleMessage = "영어와 숫자를 포함해야합니다."),
            @ErrorCase(description = "비밀번호 글자수 오류", exampleMessage = "비밀번호는 8~16자 사이로 입력해주세요."),
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/login", errorCases = {
            @ErrorCase(description = "이메일 불일치", exampleMessage = "이메일이 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 불일치", exampleMessage = "비밀번호가 입력되지 않았습니다."),
    })
    LoginResponse login(LoginRequest request, HttpServletResponse response);

    @Operation(summary = "이메일 로그인 후 쿠키 인증")
    @ApiResponse(responseCode = "200", description = "쿠키 인증 성공")
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/login/check", errorCases = {
            @ErrorCase(description = "쿠키 값 오류", exampleMessage = "인증에 실패했습니다."),
    })
    void checkLogin(HttpServletRequest request);

    @Operation(summary = "로그아웃")
    @ApiResponse(responseCode = "204", description = "인증 성공")
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/logout", errorCases = {
            @ErrorCase(description = "쿠키 값 오류", exampleMessage = "인증에 실패했습니다."),
    })
    void logout(HttpServletResponse response);
}
