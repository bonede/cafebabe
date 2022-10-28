package org.javaexplorer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = JavaFilePathValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaFilePath {
    String message() default "Invalid path";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
