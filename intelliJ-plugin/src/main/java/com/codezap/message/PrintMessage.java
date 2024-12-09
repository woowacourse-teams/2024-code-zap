package com.codezap.message;

public enum PrintMessage {
    NEED_FILE_SELECT_MESSAGE("파일을 선택한 상태에서 실행해 주세요.\n 1. 프로젝트 창에서 파일 선택 또는\n 2. 에디터에서 파일을 연 상태로 실행"),
    NEED_FILE_SELECT("파일 선택 필수"),
    SUCCESS_TEMPLATE_UPLOAD("템플릿 생성 완료"),
    SUCCESS_TEMPLATE_UPLOAD_MESSAGE("정상적으로 템플릿이 생성되었습니다."),
    FAIL_TEMPLATE_UPLOAD("템플릿 생성 실패"),
    SUCCESS_LOGIN("로그인 성공"),
    FAIL_LOGIN("로그인 실패"),
    WELCOME_MESSAGE("님 만나서 반가워요"),
    SERVER_ERROR_MESSAGE("서버의 문제로 실패하였습니다.\n 다시 시도해주세요."),
    PLUGIN_ERROR_MESSAGE("플러그인에서 문제가 발생했습니다.\n 다시 시도해주세요."),
    ;

    private final String message;

    PrintMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
