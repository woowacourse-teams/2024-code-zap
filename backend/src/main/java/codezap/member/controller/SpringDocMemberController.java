package codezap.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import codezap.global.swagger.error.ApiErrorResponse;
import codezap.global.swagger.error.ErrorCase;
import codezap.global.swagger.error.ProblemDetailSchema;
import codezap.member.dto.LoginRequest;
import codezap.member.dto.LoginResponse;
import codezap.member.dto.MemberDto;
import codezap.member.dto.SignupRequest;
import codezap.member.dto.request.UpdateMemberProfileRequest;
import codezap.member.dto.response.FindMemberResponse;
import codezap.member.dto.response.UpdateMemberProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "인증 및 인가 API", description = "회원가입 API")
public interface SpringDocMemberController {

    @Operation(summary = "회원가입")
    @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = "요청 형식 오류",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetailSchema.class),
                    examples = {
                            @ExampleObject(name = "이메일 입력 없음", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "이메일이 입력되지 않았습니다.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "이메일 형식 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "이메일 형식이 아닙니다.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "이메일 글자수 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "이메일은 255자 이하로 입력해주세요.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "비밀번호 입력 없음", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "비밀번호가 입력되지 않았습니다.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "비밀번호 형식 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "영어와 숫자를 포함해야합니다.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "비밀번호 글자수 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "비밀번호는 8~16자 사이로 입력해주세요.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "사용자명 입력 없음", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "사용자명이 입력되지 않았습니다.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "사용자명 글자수 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "사용자명이 입력되지 않았습니다.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                    }
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "이메일 또는 사용자명 중복",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetailSchema.class),
                    examples = {
                            @ExampleObject(name = "이메일 중복", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "CONFLICT",
                                      "status": 409,
                                      "detail": "이메일이 이미 존재합니다.",
                                      "instance": "/signup"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "사용자명 중복", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "CONFLICT",
                                      "status": 409,
                                      "detail": "사용자명이 이미 존재합니다.",
                                      "instance": "/signup"
                                    }
                                    """
                            )
                    }
            )
    )
    void signup(@RequestBody SignupRequest request);

    @Operation(summary = "이메일 중복 확인")
    @ApiResponse(responseCode = "200", description = "사용가능한 이메일")
    @ApiResponse(
            responseCode = "409",
            description = "중복된 이메일",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetailSchema.class),
                    examples = {
                            @ExampleObject(name = "이메일 중복", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "CONFLICT",
                                      "status": 409,
                                      "detail": "이메일이 이미 존재합니다.",
                                      "instance": "/check-email"
                                    }
                                    """
                            )
                    }
            )
    )
    void checkUniqueEmail(@RequestParam String email);

    @Operation(summary = "사용자명 중복 확인")
    @ApiResponse(responseCode = "200", description = "사용가능한 사용자명")
    @ApiResponse(
            responseCode = "409",
            description = "중복된 사용자명",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetailSchema.class),
                    examples = {
                            @ExampleObject(name = "사용자명 중복", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "CONFLICT",
                                      "status": 409,
                                      "detail": "사용자명이 이미 존재합니다.",
                                      "instance": "/check-username"
                                    }
                                    """
                            )
                    }
            )
    )
    void checkUniqueUsername(@RequestParam String username);

    @Operation(summary = "이메일 로그인")
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            headers = {
                    @Header(name = "Set-Cookie", description = "base64(${email}:${password}); path=\"/\"; HttpOnly; "
                            + "Secure;")}
    )
    @ApiResponse(
            responseCode = "400",
            description = "요청 형식 오류",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetailSchema.class),
                    examples = {
                            @ExampleObject(name = "이메일 입력 없음", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "이메일이 입력되지 않았습니다.",
                                      "instance": "/login"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "이메일 형식 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "이메일 형식이 아닙니다.",
                                      "instance": "/login"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "이메일 글자수 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "이메일은 255자 이하로 입력해주세요.",
                                      "instance": "/login"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "비밀번호 입력 없음", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "비밀번호가 입력되지 않았습니다.",
                                      "instance": "/login"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "비밀번호 형식 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "영어와 숫자를 포함해야합니다.",
                                      "instance": "/login"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "비밀번호 글자수 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "BAD_REQUEST",
                                      "status": 400,
                                      "detail": "비밀번호는 8~16자 사이로 입력해주세요.",
                                      "instance": "/login"
                                    }
                                    """
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "요청 형식 오류",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetailSchema.class),
                    examples = {
                            @ExampleObject(name = "이메일 불일치", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "UNAUTHORIZED",
                                      "status": 401,
                                      "detail": "이메일이 입력되지 않았습니다.",
                                      "instance": "/login"
                                    }
                                    """
                            ),
                            @ExampleObject(name = "비밀번호 불일치", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "UNAUTHORIZED",
                                      "status": 401,
                                      "detail": "비밀번호가 입력되지 않았습니다.",
                                      "instance": "/login"
                                    }
                                    """
                            ),
                    }
            )
    )
    LoginResponse login(LoginRequest request, HttpServletResponse response);

    @Operation(summary = "이메일 로그인 후 쿠키 인증")
    @ApiResponse(responseCode = "200", description = "쿠키 인증 성공")
    @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetailSchema.class),
                    examples = {
                            @ExampleObject(name = "쿠키 값 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "UNAUTHORIZED",
                                      "status": 401,
                                      "detail": "인증에 실패했습니다.",
                                      "instance": "/login/check"
                                    }
                                    """)
                    }
            )
    )
    void checkLogin(HttpServletRequest request);

    @Operation(summary = "로그아웃")
    @ApiResponse(responseCode = "204", description = "인증 성공")
    @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetailSchema.class),
                    examples = {
                            @ExampleObject(name = "쿠키 값 오류", value = """
                                    {
                                      "type": "about:blank",
                                      "title": "UNAUTHORIZED",
                                      "status": 401,
                                      "detail": "인증에 실패했습니다.",
                                      "instance": "/login/check"
                                    }
                                    """)
                    }
            )
    )
    void logout(HttpServletResponse response);

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "회원 정보 조회", description = "회원의 정보(이메일, 닉네임)을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공")
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/members/1", errorCases = {
            @ErrorCase(description = "쿠키 값 오류", exampleMessage = "인증에 실패했습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.FORBIDDEN, instance = "/members/1", errorCases = {
            @ErrorCase(description = "조회하려는 회원에 대해 권한이 없는 경우", exampleMessage = "권한이 없어 식별자 1에 해당하는 회원 정보를 조회할 수 없습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND, instance = "/members/1", errorCases = {
            @ErrorCase(description = "조회하려는 id 값인 회원이 없는 경우", exampleMessage = "식별자 1에 해당하는 회원이 존재하지 않습니다.")
    })
    ResponseEntity<FindMemberResponse> findMember(MemberDto memberDto, Long id);

    @SecurityRequirement(name = "쿠키 인증 토큰")
    @Operation(summary = "회원 정보 수정", description = "회원의 정보(사용자명, 비밀번호)을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/members/1", errorCases = {
            @ErrorCase(description = "사용자명 입력 없음", exampleMessage = "사용자명이 입력되지 않았습니다."),
            @ErrorCase(description = "사용자명 형식 오류", exampleMessage = "사용자명은 2~255자 사이로 입력해주세요."),
            @ErrorCase(description = "사용자명 글자수 오류", exampleMessage = "사용자명이 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 입력 없음", exampleMessage = "비밀번호가 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 형식 오류", exampleMessage = "영어와 숫자를 포함해야합니다."),
            @ErrorCase(description = "비밀번호 글자수 오류", exampleMessage = "비밀번호는 8~16자 사이로 입력해주세요."),
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/members/1", errorCases = {
            @ErrorCase(description = "쿠키 값 오류", exampleMessage = "인증에 실패했습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.FORBIDDEN, instance = "/members/1", errorCases = {
            @ErrorCase(description = "조회하려는 회원에 대해 권한이 없는 경우", exampleMessage = "권한이 없어 식별자 1에 해당하는 회원 정보를 수정할 수 없습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND, instance = "/members/1", errorCases = {
            @ErrorCase(description = "수정하려는 id 값인 회원이 없는 경우", exampleMessage = "식별자 1에 해당하는 회원이 존재하지 않습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.CONFLICT, instance = "/members/1", errorCases = {
            @ErrorCase(description = "사용자명 중복", exampleMessage = "사용자명이 이미 존재합니다."),
    })
    ResponseEntity<UpdateMemberProfileResponse> updateMemberProfile(UpdateMemberProfileRequest updateMemberProfileRequest, Long id);
}
