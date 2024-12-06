package com.codezap.service;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.codezap.client.CodeZapClient;
import com.codezap.client.HttpMethod;
import com.codezap.dto.request.LoginRequest;
import com.codezap.dto.response.LoginResponse;
import com.codezap.exception.ErrorType;
import com.codezap.exception.PluginException;
import com.codezap.panel.LoginInputPanel;
import com.intellij.openapi.ui.Messages;

public class LoginService {

    public static final String SUCCESS_LOGIN = "로그인 성공";
    public static final String FAIL_LOGIN = "로그인 실패";
    public static final String SERVER_ERROR_MESSAGE = "서버의 문제로 로그인 실패하였습니다.\n 다시 시도해주세요.";
    public static final String WELCOME_MESSAGE = "님 만나서 반가워요.";
    private static final String LOGIN_URL = "/login";

    private LoginResponse loginResponse;

    public boolean login() {
        if (CodeZapClient.existsCookie()) {
            Messages.showInfoMessage(loginResponse.name() + WELCOME_MESSAGE, SUCCESS_LOGIN);
            return true;
        }

        try {
            LoginRequest loginRequest = LoginInputPanel.inputLogin();
            setLoginResponse(login(loginRequest));
            Messages.showInfoMessage(loginResponse.name() + WELCOME_MESSAGE, SUCCESS_LOGIN);
            return true;
        } catch (IOException ignored) {
            Messages.showInfoMessage(SERVER_ERROR_MESSAGE, FAIL_LOGIN);
        } catch (PluginException e) {
            if (!e.matchErrorType(ErrorType.CANCEL_TAP)) {
                Messages.showInfoMessage(e.getMessage(), FAIL_LOGIN);
            }
        }
        return false;
    }

    private LoginResponse login(LoginRequest request) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = CodeZapClient.getHttpURLConnection(LOGIN_URL, HttpMethod.POST, request);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new PluginException(CodeZapClient.getErrorMessage(connection), responseCode);
            }
            CodeZapClient.setCookie(connection);
            return CodeZapClient.makeResponse(connection, jsonResponse ->
                    new LoginResponse(jsonResponse.get("memberId").asLong(), jsonResponse.get("name").asText()));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public long getMemberId() {
        return loginResponse.memberId();
    }

    private void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }
}
