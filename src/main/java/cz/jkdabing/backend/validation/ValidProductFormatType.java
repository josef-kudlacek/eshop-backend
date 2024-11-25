package cz.jkdabing.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductFormatType {
    String message() default "Invalid product format type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
