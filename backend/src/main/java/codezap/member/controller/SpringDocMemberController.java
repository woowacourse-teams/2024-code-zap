package codezap.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.member.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "인증 및 인가 API", description = "회원가입 API")
public interface SpringDocMemberController {

    @Operation(summary = "회원가입")
    @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            headers = {@Header(name = "Location", example = "/members/1")}
    )
    @ApiErrorResponse(
            status = HttpStatus.CONFLICT,
            instance = "/signup",
            errorCases = {
                    @ErrorCase(description = "이메일 중복", exampleMessage = "이메일이 이미 존재합니다."),
                    @ErrorCase(description = "사용자명 중복", exampleMessage = "사용자명이 이미 존재합니다.")
            }
    )
    ResponseEntity<Void> signup(@RequestBody SignupRequest request);

    @Operation(summary = "이메일 중복 확인")
    @ApiResponse(responseCode = "200", description = "사용가능한 이메일이면 true, 중복된 이메일이면 false를 반환합니다.")
    ResponseEntity<Boolean> checkUniqueEmail(@RequestParam String email);

    @Operation(summary = "사용자명 중복 확인")
    @ApiResponse(responseCode = "200", description = "사용가능한 사용자명이면 true, 중복된 사용자명이면 false를 반환합니다.")
    ResponseEntity<Boolean> checkUniqueUsername(@RequestParam String username);
}
