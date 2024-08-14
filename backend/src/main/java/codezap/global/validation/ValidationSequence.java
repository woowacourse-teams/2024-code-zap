package codezap.global.validation;

import jakarta.validation.GroupSequence;

import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;
import codezap.global.validation.ValidationGroups.SourceCodeCountGroup;
import codezap.global.validation.ValidationGroups.SourceCodeOrdinalGroup;

@GroupSequence({
        NotNullGroup.class,
        SizeCheckGroup.class,
        SourceCodeCountGroup.class,
        SourceCodeOrdinalGroup.class})
public interface ValidationSequence {
}
