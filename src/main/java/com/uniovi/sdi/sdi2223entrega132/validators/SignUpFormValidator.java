package com.uniovi.sdi.sdi2223entrega132.validators;

import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SignUpFormValidator implements Validator {
    @Autowired
    private UsersService usersService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "error.empty");
        if (user.getEmail().length() < 3) {
            errors.rejectValue("email", "error.user.email.length");
        }
        if (!user.getEmail().contains("@")) {
            errors.rejectValue("email", "error.user.email.format");
        }
        if (usersService.getUserByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "error.user.email.duplicate");
        }
        if (user.getPassword().length() < 5 || user.getPassword().length() > 24) {
            errors.rejectValue("password", "error.user.password.length");
        }
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm",
                    "error.user.passwordConfirm.coincidence");
        }
    }
}