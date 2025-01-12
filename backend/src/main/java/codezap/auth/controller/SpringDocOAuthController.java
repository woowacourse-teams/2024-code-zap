package codezap.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codezap.auth.dto.request.OAuthSignupRequest;
import codezap.auth.dto.request.OAuthUniqueCheckRequest;
import codezap.auth.dto.response.OAuthUniqueCheckResponse;
import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface SpringDocOAuthController {

    @Operation(summary = "소셜 계정 중복 확인")
    @ApiResponse(responseCode = "200", description = "중복 확인 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/oauth/check-unique", errorCases = {
            @ErrorCase(description = "OAuth 제공자 존재하지 않음", exampleMessage = "OAuth 제공자가 입력되지 않았습니다."),
            @ErrorCase(description = "잘못된 OAuth 제공자 입력", exampleMessage = "OAuth 제공자가 잘못되었습니다."),
            @ErrorCase(description = "authorization code 존재하지 않음", exampleMessage = "authorization code를 입력해주세요"),
            @ErrorCase(description = "authorization code 오류", exampleMessage = "올바르지 않은 authorization code 입니다."),
    })
    ResponseEntity<OAuthUniqueCheckResponse> checkUnique(OAuthUniqueCheckRequest request);

    @Operation(summary = "소셜 회원가입")
    @ApiResponse(responseCode = "200", description = "소셜 회원가입 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/oauth/signup", errorCases = {
            @ErrorCase(description = "OAuth 제공자 존재하지 않음", exampleMessage = "OAuth 제공자가 입력되지 않았습니다."),
            @ErrorCase(description = "잘못된 OAuth 제공자 입력", exampleMessage = "OAuth 제공자가 잘못되었습니다."),
            @ErrorCase(description = "authorization code 존재하지 않음", exampleMessage = "authorization code를 입력해주세요"),
            @ErrorCase(description = "authorization code 오류", exampleMessage = "올바르지 않은 authorization code 입니다."),
            @ErrorCase(description = "아이디 입력 없음", exampleMessage = "아이디이 입력되지 않았습니다."),
            @ErrorCase(description = "아이디 글자수 오류", exampleMessage = "아이디은 255자 이하로 입력해주세요."),
    })
    @ApiErrorResponse(status = HttpStatus.CONFLICT, instance = "/oauth/signup", errorCases = {
            @ErrorCase(description = "아이디 중복", exampleMessage = "아이디가 이미 존재합니다."),
    })
    ResponseEntity<Void> signup(OAuthSignupRequest request);
}
