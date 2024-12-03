package com.codezap.service;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.codezap.client.CodeZapClient;
import com.codezap.client.HttpMethod;
import com.codezap.dto.request.TemplateCreateRequest;
import com.codezap.exception.PluginException;

public class TemplateService {

    private static final String TEMPLATES_URL = "/templates";

    public void createTemplate(TemplateCreateRequest request) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = CodeZapClient.getHttpURLConnection(TEMPLATES_URL, HttpMethod.POST, request);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_CREATED) {
                throw new PluginException(CodeZapClient.getErrorMessage(connection), responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
