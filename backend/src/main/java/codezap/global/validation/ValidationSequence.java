package codezap.global.validation;

import jakarta.validation.GroupSequence;

import codezap.global.validation.ValidationGroups.ByteLengthGroup;
import codezap.global.validation.ValidationGroups.NotNullGroup;
import codezap.global.validation.ValidationGroups.SizeCheckGroup;

@GroupSequence({NotNullGroup.class, SizeCheckGroup.class, ByteLengthGroup.class})
public interface ValidationSequence {
}