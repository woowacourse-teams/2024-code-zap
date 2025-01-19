package codezap.global.validation;

import jakarta.validation.GroupSequence;

import codezap.global.validation.ValidationGroups.DuplicateIdGroup;
import codezap.global.validation.ValidationGroups.DuplicateNameGroup;
import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.OrdinalGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import codezap.global.validation.ValidationGroups.SourceCodeCountGroup;

@GroupSequence({
        NotNullGroup.class,
        SizeCheckGroup.class,
        SourceCodeCountGroup.class,
        OrdinalGroup.class,
        DuplicateIdGroup.class,
        DuplicateNameGroup.class})
public interface ValidationSequence {
}
