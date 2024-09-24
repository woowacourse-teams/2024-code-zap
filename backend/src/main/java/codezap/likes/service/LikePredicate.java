package codezap.likes.service;

import codezap.template.domain.Template;

@FunctionalInterface
public interface LikePredicate {

    boolean isLike(Template template);
}
