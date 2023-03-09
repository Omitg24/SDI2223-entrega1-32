package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.OffersService;
import com.uniovi.sdi.sdi2223entrega132.validators.AddOfferFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Date;

@Controller
public class OffersController {
    @Autowired
    private OffersService offersService;

    @Autowired
    private AddOfferFormValidator addOfferFormValidator;

    @RequestMapping(value = "/offer/add", method = RequestMethod.GET)
    public String setOffer(Model model) {
        model.addAttribute("offer", new Offer());
        return "offer/add";
    }
    @RequestMapping(value = "/offer/add", method = RequestMethod.POST)
    public String setOffer(@ModelAttribute @Validated Offer offer, Principal principal, BindingResult result) {
        addOfferFormValidator.validate(offer, result);
        System.out.println(offer.getDescription());
        if (result.hasErrors()) {
            return "offer/add";
        }
        String email = principal.getName();
        offer.setUploadDate(new Date());
        //User owner = usersService.getUserByEmail(email);
        //offer.setOwner(owner);
        offersService.addOffer(offer);
        return "redirect:/offer/ownedList";
    }


}
