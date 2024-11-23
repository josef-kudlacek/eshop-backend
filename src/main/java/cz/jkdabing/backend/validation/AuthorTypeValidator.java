package cz.jkdabing.backend.validation;

import cz.jkdabing.backend.enums.AuthorType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AuthorTypeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String authorType = (String) target;

        try {
            AuthorType.valueOf(authorType);
        } catch (IllegalArgumentException e) {
            errors.rejectValue("authorType", "authorType.invalid", "Invalid author type: " + authorType);
        }
    }
}

