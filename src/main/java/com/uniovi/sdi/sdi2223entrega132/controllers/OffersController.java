package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import com.uniovi.sdi.sdi2223entrega132.entities.Message;
import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.OffersService;
import com.uniovi.sdi.sdi2223entrega132.services.UsersService;
import com.uniovi.sdi.sdi2223entrega132.validators.AddOfferFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;

@Controller
public class OffersController {
    @Autowired
    private OffersService offersService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private AddOfferFormValidator addOfferFormValidator;

    @RequestMapping(value = "/offer/searchList", method = RequestMethod.GET)
    public String getOwnedList(Model model, Pageable pageable,
                               @RequestParam(value = "", required = false) String searchText) {

        Page<Offer> offers = new PageImpl<Offer>(new LinkedList<Offer>());
        model.addAttribute("searchText", "");
        if (searchText != null && !searchText.isEmpty()) {
            model.addAttribute("searchText", searchText);
            offers = offersService.searchOffersByTitle(pageable, searchText);
        } else {
            offers = offersService.getAvailableOffers(pageable);
        }
        model.addAttribute("offersList", offers.getContent());
        model.addAttribute("page", offers);
        return "offer/searchList";
    }

    @RequestMapping(value = "/offer/ownedList", method = RequestMethod.GET)
    public String getOwnedList(Model model, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        model.addAttribute("offersList", offersService.getOffersOfUser(user));
        return "offer/ownedList";
    }

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
        offer.setPurchase(true);
        User owner = usersService.getUserByEmail(email);
        offer.setOwner(owner);
        offersService.addOffer(offer);
        return "redirect:/offer/ownedList";
    }

    @RequestMapping(value = "/offer/delete/{id}", method = RequestMethod.GET)
    public String deleteOffer(@PathVariable Long id) {
        offersService.deleteOffer(id);
        return "redirect:/offer/ownedList";
    }


}
