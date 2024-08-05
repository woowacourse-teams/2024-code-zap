package codezap.global.validation;

import jakarta.validation.GroupSequence;

import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import codezap.global.validation.ValidationGroups.SnippetCountGroup;
import codezap.global.validation.ValidationGroups.SnippetOrdinalGroup;

@GroupSequence({
        NotNullGroup.class,
        SizeCheckGroup.class,
        SnippetCountGroup.class,
        SnippetOrdinalGroup.class})
public interface ValidationSequence {
}
