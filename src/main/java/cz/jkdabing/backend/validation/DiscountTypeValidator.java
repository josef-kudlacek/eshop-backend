package cz.jkdabing.backend.validation;

import cz.jkdabing.backend.enums.DiscountType;
import cz.jkdabing.backend.validation.annotation.ValidDiscountType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DiscountTypeValidator implements ConstraintValidator<ValidDiscountType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(value)) {
            addErrorMessage(constraintValidatorContext, "Discount type cannot be null");
            return false;
        }

        try {
            DiscountType.valueOf(value);
        } catch (IllegalArgumentException e) {
            addErrorMessage(constraintValidatorContext, "Invalid discount type: " + value);
            return false;
        }

        return true;
    }

    private void addErrorMessage(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addConstraintViolation();
    }
}
