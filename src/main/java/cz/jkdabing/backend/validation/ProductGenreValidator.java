package cz.jkdabing.backend.validation;

import cz.jkdabing.backend.enums.ProductGenreType;
import cz.jkdabing.backend.validation.annotation.ValidProductGenre;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProductGenreValidator implements ConstraintValidator<ValidProductGenre, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(value)) {
            addErrorMessage(constraintValidatorContext, "Product genre cannot be null");
            return false;
        }

        try {
            ProductGenreType.valueOf(value);
        } catch (IllegalArgumentException e) {
            addErrorMessage(constraintValidatorContext, "Invalid product genre: " + value);
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

