package com.uniovi.sdi.sdi2223entrega132.validators;

import com.uniovi.sdi.sdi2223entrega132.entities.Message;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class SendMessageValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Message.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Message message = (Message) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "error.message.empty");
    }
}
