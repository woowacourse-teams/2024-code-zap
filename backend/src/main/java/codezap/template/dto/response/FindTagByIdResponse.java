package codezap.template.dto.response;

import codezap.template.domain.Tag;

public record FindTagByIdResponse(
        Long id,
        String name
) {
    public static FindTagByIdResponse from(Tag tag) {
        return new FindTagByIdResponse(tag.getId(), tag.getName());
    }
}
