package cz.jkdabing.backend.validation.annotation;

import cz.jkdabing.backend.validation.ProductGenreValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductGenreValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductGenre {
    String message() default "Invalid product genre";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
