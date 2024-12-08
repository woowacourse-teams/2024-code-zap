package com.codezap.message;

public enum ApiEndpoints {

    BASE_URL("https://api.code-zap.com"),
    LOGIN_URL("/login"),
    TEMPLATES_URL("/templates"),
    CATEGORIES_URL("/categories?memberId="),
    ;

    private final String url;

    ApiEndpoints(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}
