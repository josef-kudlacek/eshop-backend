package cz.jkdabing.backend.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private List<String> errorMessages;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        errorMessages = new ArrayList<>();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(password)) {
            errorMessages.add("Password cannot be null");
            return isPasswordWithoutError(context);
        }

        boolean hasMinLength = password.length() >= 8;
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

        if (!hasMinLength) {
            errorMessages.add("Password must be at least 8 characters long");
        }
        if (!hasLowerCase) {
            errorMessages.add("Password must contain at least one lowercase letter");
        }
        if (!hasDigit) {
            errorMessages.add("Password must contain at least one digit");
        }
        if (!hasUpperCase) {
            errorMessages.add("Password must contain at least one uppercase letter");
        }
        if (!hasSpecialChar) {
            errorMessages.add("Password must contain at least one special character");
        }

        return isPasswordWithoutError(context);
    }

    private boolean isPasswordWithoutError(ConstraintValidatorContext context) {
        if (errorMessages.isEmpty()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        for (String errorMessage : errorMessages) {
            context.buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation();
        }

        errorMessages = new ArrayList<>();
        return false;
    }
}
