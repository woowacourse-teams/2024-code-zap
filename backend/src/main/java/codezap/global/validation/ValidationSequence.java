package codezap.global.validation;

import jakarta.validation.GroupSequence;

import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import codezap.global.validation.ValidationGroups.SourceCodeCountGroup;
import codezap.global.validation.ValidationGroups.OrdinalGroup;

@GroupSequence({
        NotNullGroup.class,
        SizeCheckGroup.class,
        SourceCodeCountGroup.class,
        OrdinalGroup.class})
public interface ValidationSequence {
}
