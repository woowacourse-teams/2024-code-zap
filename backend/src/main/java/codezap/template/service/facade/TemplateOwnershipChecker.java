package codezap.template.service.facade;

import codezap.template.domain.Template;

@FunctionalInterface
public interface TemplateOwnershipChecker {

    boolean isOwner(Template template);
}
