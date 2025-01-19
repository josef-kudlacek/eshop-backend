package cz.jkdabing.backend.validation.annotation;

import cz.jkdabing.backend.validation.DiscountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DiscountValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDiscount {
    String message() default "Invalid discount";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
