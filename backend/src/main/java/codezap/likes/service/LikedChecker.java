package codezap.likes.service;

import codezap.template.domain.Template;

@FunctionalInterface
public interface LikedChecker {

    boolean isLiked(Template template);
}
