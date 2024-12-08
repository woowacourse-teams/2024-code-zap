package com.codezap.dto.request;

public record CreateSourceCodeRequest(
        String filename,
        String content,
        int ordinal
) {
}
