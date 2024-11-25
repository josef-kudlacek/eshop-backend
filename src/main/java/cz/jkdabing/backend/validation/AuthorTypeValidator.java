package cz.jkdabing.backend.validation;

import cz.jkdabing.backend.enums.AuthorType;
import cz.jkdabing.backend.validation.annotation.ValidAuthorType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthorTypeValidator implements ConstraintValidator<ValidAuthorType, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtils.hasText(value)) {
            addErrorMessage(constraintValidatorContext, "Author type cannot be null");
            return false;
        }

        try {
            AuthorType.valueOf(value);
        } catch (IllegalArgumentException e) {
            addErrorMessage(constraintValidatorContext, "Invalid author type: " + value);
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

