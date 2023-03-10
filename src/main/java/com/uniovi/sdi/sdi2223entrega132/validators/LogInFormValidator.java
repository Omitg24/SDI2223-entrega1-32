package com.uniovi.sdi.sdi2223entrega132.validators;

import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LogInFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.empty");
        if (user.getEmail().length() < 3) {
            errors.rejectValue("email", "error.user.email.length");
        }
        if (!user.getEmail().contains("@")) {
            errors.rejectValue("email", "error.user.email.format");
        }
        if (user.getPassword().length() < 5 || user.getPassword().length() > 24) {
            errors.rejectValue("password", "error.user.password.length");
        }
    }
}
