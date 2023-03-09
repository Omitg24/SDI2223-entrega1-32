package com.uniovi.sdi.sdi2223entrega132.validators;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddOfferFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Offer.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Offer offer = (Offer) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "Error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "Error.empty");

        if (offer.getTitle().length() < 3 || offer.getTitle().length() > 30) {
            errors.rejectValue("title", "Error.title.length");
        }
        if (offer.getDescription().length() < 10 || offer.getDescription().length() > 200) {
            errors.rejectValue("description", "Error.description.length");
        }
        if (offer.getPrice() < 0) {
            errors.rejectValue("price", "Error.price.positive");
        }


    }
}
