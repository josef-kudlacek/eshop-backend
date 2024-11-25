package cz.jkdabing.backend.validation;

import cz.jkdabing.backend.enums.ProductFormatType;
import cz.jkdabing.backend.validation.annotation.ValidProductFormatType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProductFormatValidator implements ConstraintValidator<ValidProductFormatType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(value)) {
            addErrorMessage(constraintValidatorContext, "Product format type cannot be null");
            return false;
        }

        try {
            ProductFormatType.valueOf(value);
        } catch (IllegalArgumentException e) {
            addErrorMessage(constraintValidatorContext, "Invalid product format type: " + value);
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

