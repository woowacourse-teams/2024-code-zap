package com.codezap.dto.request;

public record LoginRequest(
        String name,
        String password
) {
}
