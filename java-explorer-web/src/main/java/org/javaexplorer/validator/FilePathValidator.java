package org.javaexplorer.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FilePathValidator implements
        ConstraintValidator<FilePath, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO implement
        return true;
    }
}
