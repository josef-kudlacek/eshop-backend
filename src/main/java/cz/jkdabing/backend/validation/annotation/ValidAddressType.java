package cz.jkdabing.backend.validation.annotation;

import cz.jkdabing.backend.validation.AddressTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AddressTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAddressType {
    String message() default "Invalid address type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
