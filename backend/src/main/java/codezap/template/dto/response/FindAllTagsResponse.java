package codezap.template.dto.response;

import java.util.List;

public record FindAllTagsResponse(
        List<FindTagResponse> tags
) {
}
