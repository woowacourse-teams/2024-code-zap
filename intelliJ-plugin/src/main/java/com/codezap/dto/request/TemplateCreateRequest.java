package com.codezap.dto.request;

import java.util.List;

public record TemplateCreateRequest(
        String title,
        String description,
        List<CreateSourceCodeRequest> sourceCodes,
        Integer thumbnailOrdinal,
        Long categoryId,
        List<String> tags,
        String visibility
) {
}
