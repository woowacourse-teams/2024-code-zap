package com.codezap.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.codezap.client.CodeZapClient;
import com.codezap.client.HttpMethod;
import com.codezap.dto.response.FindAllCategoriesResponse;
import com.codezap.dto.response.FindCategoryResponse;
import com.codezap.exception.PluginException;
import com.fasterxml.jackson.databind.JsonNode;

public class CategoryService {

    private static final String CATEGORIES_URL = "/categories?memberId=";

    public FindAllCategoriesResponse getCategories(long memberId) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = CodeZapClient.getHttpURLConnection(CATEGORIES_URL + memberId, HttpMethod.GET, null);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new PluginException(CodeZapClient.getErrorMessage(connection), responseCode);
            }
            return CodeZapClient.makeResponse(connection, (this::makeCategoriesResponse));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private FindAllCategoriesResponse makeCategoriesResponse(JsonNode jsonResponse) {
        List<FindCategoryResponse> categories = new ArrayList<>();
        JsonNode categoriesNode = jsonResponse.get("categories");

        for (JsonNode categoryNode : categoriesNode) {
            long id = categoryNode.get("id").asLong();
            String name = categoryNode.get("name").asText();
            categories.add(new FindCategoryResponse(id, name));
        }
        return new FindAllCategoriesResponse(categories);
    }
}
