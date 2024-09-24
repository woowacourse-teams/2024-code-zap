package codezap.likes.service;

import codezap.template.domain.Template;

@FunctionalInterface
public interface LikeChecker {

    boolean isLike(Template template);
}
