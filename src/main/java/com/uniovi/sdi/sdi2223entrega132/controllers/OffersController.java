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

    private boolean invalidBuy = false;
    private boolean invalidFeature = false;


    @RequestMapping(value = "/offer/searchList", method = RequestMethod.GET)
    public String getSearchList(Model model, Pageable pageable, Principal principal,
                               @RequestParam(value = "", required = false) String searchText) {
        String email = principal.getName();
        User interestedUser = usersService.getUserByEmail(email);
        Page<Offer> offers = new PageImpl<Offer>(new LinkedList<Offer>());
        model.addAttribute("searchText", "");
        if (searchText != null && !searchText.isEmpty()) {
            model.addAttribute("searchText", searchText);
            offers = offersService.searchOffersByTitle(pageable, searchText);
        } else {
            offers = offersService.getAvailableOffers(pageable);
        }
        model.addAttribute("interestedUser",interestedUser);
        model.addAttribute("offersList", offers.getContent());
        model.addAttribute("page", offers);
        model.addAttribute("buyError",invalidBuy);
        invalidBuy=false;
        return "offer/searchListCard";
    }

    @RequestMapping(value = "/offer/searchList/update", method = RequestMethod.GET)
    public String getSearchListUpdate(Model model, Pageable pageable) {
        Page<Offer> offers = new PageImpl<Offer>(new LinkedList<Offer>());
        model.addAttribute("searchText", "");
        offers = offersService.getAvailableOffers(pageable);
        model.addAttribute("offersList", offers.getContent());
        model.addAttribute("page", offers);
        model.addAttribute("buyError",invalidBuy);
        invalidBuy=false;
        return "offer/searchList :: tableSearchedOffers";
    }

    @RequestMapping(value = "/offer/ownedList", method = RequestMethod.GET)
    public String getOwnedList(Model model, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        model.addAttribute("offersList", offersService.getOffersOfUser(user));
        model.addAttribute("featuredList", offersService.getOffersFeatured());
        model.addAttribute("featureError",invalidFeature);
        invalidFeature=false;
        return "offer/ownedListCard";
    }

    @RequestMapping(value = "/offer/ownedList/update", method = RequestMethod.GET)
    public String getOwnedListUpdate(Model model,Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        model.addAttribute("offersList", offersService.getOffersOfUser(user));
        model.addAttribute("featuredList", offersService.getOffersFeatured());
        model.addAttribute("featureError",invalidFeature);
        invalidFeature=false;
        return "offer/ownedList :: tableOwnedOffers";
    }

    @RequestMapping(value = "/offer/add", method = RequestMethod.GET)
    public String setOffer(Model model) {
        model.addAttribute("offer", new Offer());
        return "offer/add";
    }

    @RequestMapping(value = "/offer/add", method = RequestMethod.POST)
    public String setOffer(@ModelAttribute @Validated Offer offer, Principal principal, BindingResult result) {
        String email = principal.getName();
        offer.setUploadDate(new Date());
        User owner = usersService.getUserByEmail(email);
        offer.setOwner(owner);
        addOfferFormValidator.validate(offer, result);
        if (result.hasErrors()) {
            return "offer/add";
        }
        offersService.addOffer(offer);
        return "redirect:/offer/ownedList";
    }

    @RequestMapping(value = "/offer/delete/{id}", method = RequestMethod.GET)
    public String deleteOffer(@PathVariable Long id) {
        offersService.deleteOffer(id);
        return "redirect:/offer/ownedList";
    }

    @RequestMapping(value = "/offer/{id}/purchase", method = RequestMethod.GET)
    public String setPurchaseTrue(@PathVariable Long id,Model model) {
        invalidBuy=offersService.validatePurchase(id);
        if(!invalidBuy){
            offersService.setOfferPurchase(true, id);
        }
        return "offer/searchList :: tableSearchedOffers";
    }

    @RequestMapping(value = "/offer/{id}/feature", method = RequestMethod.GET)
    public String setFeatureTrue(@PathVariable Long id) {
        invalidFeature=offersService.validateFeature(id);
        if(!invalidFeature){
            offersService.setOfferFeature(true, id);
        }
        return "offer/ownedList :: tableOwnedOffers";
    }

    @RequestMapping(value = "/offer/purchasedList", method = RequestMethod.GET)
    public String getPurchasedList(Model model, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        model.addAttribute("offersList", offersService.getOffersOfBuyer(user));
        return "offer/purchasedList";
    }

}
