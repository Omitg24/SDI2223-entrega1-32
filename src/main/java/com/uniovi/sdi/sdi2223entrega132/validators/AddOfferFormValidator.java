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
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "error.empty");

        if (offer.getTitle().length() < 3 || offer.getTitle().length() > 30) {
            errors.rejectValue("title", "error.offer.title.length");
        }
        if (offer.getDescription().length() < 10 || offer.getDescription().length() > 200) {
            errors.rejectValue("description", "error.offer.description.length");
        }
        if (offer.getPrice() < 0) {
            errors.rejectValue("price", "error.offer.price.positive");
        }
        if (offer.isFeatured() && offer.getOwner().getAmount() <= 20) {
            errors.rejectValue("featured", "error.offer.featured");
        }


    }
}
