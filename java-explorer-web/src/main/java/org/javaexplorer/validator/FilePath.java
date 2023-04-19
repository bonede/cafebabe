package org.javaexplorer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilePathValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FilePath {
    String message() default "Invalid file path";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
