package cz.jkdabing.backend.validation.annotation;

import cz.jkdabing.backend.validation.AuthorTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AuthorTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAuthorType {
    String message() default "Invalid author type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
