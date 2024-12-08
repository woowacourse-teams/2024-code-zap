package com.codezap.service;

import static com.codezap.message.PrintMessage.FAIL_LOGIN;
import static com.codezap.message.PrintMessage.SERVER_ERROR_MESSAGE;
import static com.codezap.message.PrintMessage.SUCCESS_LOGIN;
import static com.codezap.message.PrintMessage.WELCOME_MESSAGE;
import static com.codezap.message.ApiEndpoints.LOGIN_URL;

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

    private LoginResponse loginResponse;

    public boolean login() {
        if (CodeZapClient.existsCookie()) {
            Messages.showInfoMessage(loginResponse.name() + WELCOME_MESSAGE.getMessage(), SUCCESS_LOGIN.getMessage());
            return true;
        }

        try {
            LoginRequest loginRequest = LoginInputPanel.inputLogin();
            setLoginResponse(login(loginRequest));
            Messages.showInfoMessage(loginResponse.name() + WELCOME_MESSAGE.getMessage(), SUCCESS_LOGIN.getMessage());
            return true;
        } catch (IOException ignored) {
            Messages.showInfoMessage(SERVER_ERROR_MESSAGE.getMessage(), FAIL_LOGIN.getMessage());
        } catch (PluginException e) {
            if (!e.matchErrorType(ErrorType.CANCEL_TAP)) {
                Messages.showInfoMessage(e.getMessage(), FAIL_LOGIN.getMessage());
            }
        }
        return false;
    }

    private LoginResponse login(LoginRequest request) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = CodeZapClient.getHttpURLConnection(LOGIN_URL.getURL(), HttpMethod.POST, request);

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
