package cz.jkdabing.backend.validation;

import cz.jkdabing.backend.enums.AddressType;
import cz.jkdabing.backend.validation.annotation.ValidAddressType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class AddressTypeValidator implements ConstraintValidator<ValidAddressType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(value)) {
            addErrorMessage(constraintValidatorContext, "Address type cannot be null");
            return false;
        }

        try {
            AddressType.valueOf(value);
        } catch (IllegalArgumentException e) {
            addErrorMessage(constraintValidatorContext, "Invalid address type: " + value);
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
