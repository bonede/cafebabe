package org.javaexplorer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JavaFilePathValidator implements
        ConstraintValidator<JavaFilePath, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO implement
        return true;
    }
}
