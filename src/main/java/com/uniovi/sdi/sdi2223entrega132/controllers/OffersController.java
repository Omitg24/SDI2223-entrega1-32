package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.OffersService;
import com.uniovi.sdi.sdi2223entrega132.services.UsersService;
import com.uniovi.sdi.sdi2223entrega132.validators.AddOfferFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;

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

    /**
     * Método para obtener la vista de las ofertas que pueden ser compradas
     *
     * @param model      modelo
     * @param pageable   pagina
     * @param searchText texto de busqueda
     * @return vista de las ofertas
     */
    @RequestMapping(value = "/offer/searchList", method = RequestMethod.GET)
    public String getSearchList(Model model, Pageable pageable, Principal principal,
                                @RequestParam(required = false) String searchText) {
        String email = principal.getName();
        User interestedUser = usersService.getUserByEmail(email);
        Page<Offer> offers;
        model.addAttribute("searchText", "");
        if (searchText != null && !searchText.isEmpty()) {
            model.addAttribute("searchText", searchText);
            offers = offersService.searchOffersByTitle(pageable, searchText);
        } else {
            offers = offersService.getAvailableOffers(pageable);
        }
        model.addAttribute("amount", interestedUser.getAmount());
        model.addAttribute("interestedUser", interestedUser);
        model.addAttribute("offersList", offers.getContent());
        model.addAttribute("page", offers);
        model.addAttribute("buyError", invalidBuy);
        invalidBuy = false;
        return "offer/searchList";
    }

    /**
     * Método para actualizar la lista de ofertas con busqueda
     *
     * @param model     modelo
     * @param pageable  pagina
     * @param principal principal
     * @return fragmento de la tabla de ofertas actualizada
     */
    @RequestMapping(value = "/offer/searchList/update", method = RequestMethod.GET)
    public String getSearchListUpdate(Model model, Pageable pageable, Principal principal) {
        String email = principal.getName();
        User interestedUser = usersService.getUserByEmail(email);
        Page<Offer> offers;
        model.addAttribute("searchText", "");
        offers = offersService.getAvailableOffers(pageable);
        model.addAttribute("offersList", offers.getContent());
        model.addAttribute("page", offers);
        model.addAttribute("interestedUser", interestedUser);
        model.addAttribute("amount", interestedUser.getAmount());
        model.addAttribute("buyError", invalidBuy);
        invalidBuy = false;
        return "offer/searchList :: tableSearchedOffers";
    }

    /**
     * Método para obtener la vista de las ofertas propias del usuario
     *
     * @param model     modelo
     * @param pageable  pagina
     * @param principal objeto para obtener los datos del usuario autenticado
     * @return vista de las ofertas propias
     */
    @RequestMapping(value = "/offer/ownedList", method = RequestMethod.GET)
    public String getOwnedList(Model model, Pageable pageable, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        Page<Offer> offers = offersService.getOffersOfUser(pageable, user);
        model.addAttribute("offersList", offers.getContent());
        model.addAttribute("featuredList", offersService.getOffersFeatured());
        model.addAttribute("featureError", invalidFeature);
        model.addAttribute("amount", user.getAmount());
        invalidFeature = false;
        model.addAttribute("page", offers);
        return "offer/ownedList";
    }

    /**
     * Método para actualizar la lista de ofertas propias
     *
     * @param model     modelo
     * @param pageable  pagina
     * @param principal objeto para obtener los datos del usuario autenticado
     * @return fragmento de las ofertas propias actualizado
     */
    @RequestMapping("/offer/ownedList/update")
    public String updateOwnedList(Model model, Pageable pageable, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        Page<Offer> offers = offersService.getOffersOfUser(pageable, user);
        model.addAttribute("offersList", offers.getContent());
        model.addAttribute("featuredList", offersService.getOffersFeatured());
        model.addAttribute("featureError", invalidFeature);
        model.addAttribute("amount", user.getAmount());
        invalidFeature = false;
        return "offer/ownedList :: tableOwnedOffers";
    }

    /**
     * Método para obtener la vista de añadir ofertas
     *
     * @param model modelo
     * @return vista del formulario de añadir ofertas
     */
    @RequestMapping(value = "/offer/add", method = RequestMethod.GET)
    public String setOffer(Model model) {
        model.addAttribute("offer", new Offer());
        return "offer/add";
    }

    /**
     * Méetodo encargado de validar los datos de la oferta y crearla
     *
     * @param offer     datos oferta
     * @param principal principal
     * @param result    result
     * @return vista de las ofertas si se añade o del formulario en caso de error
     */
    @RequestMapping(value = "/offer/add", method = RequestMethod.POST)
    public String setOffer(@ModelAttribute @Validated Offer offer, Principal principal, BindingResult result) {
        String email = principal.getName();
        User owner = usersService.getUserByEmail(email);
        offer.setOwner(owner);
        addOfferFormValidator.validate(offer, result);
        if (result.hasErrors()) {
            return "offer/add";
        }
        if (offer.isFeatured()) {
            usersService.updateAmount(owner.getId(), owner.getAmount() - 20);
        }
        offer.setUploadDate(new Date());
        offersService.addOffer(offer);
        if (!offer.getPicture().isEmpty()) {
            offer.setHasPicture(true);
            try {
                InputStream is = offer.getPicture().getInputStream();
                Files.copy(is, Paths.get("src/main/resources/static/pictures/" + offer.getId() + ".png"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            offersService.addOffer(offer);
        }
        return "redirect:/offer/ownedList";
    }

    /**
     * Método encargado de borrar una oferta
     *
     * @param id id de la oferta a borrar
     * @return vista de la lista de ofertas actualizada
     */
    @RequestMapping(value = "/offer/delete/{id}", method = RequestMethod.GET)
    public String deleteOffer(@PathVariable Long id) {
        offersService.deleteOffer(id);
        return "redirect:/offer/ownedList";
    }

    /**
     * Método encargado de poner la oferta como comprada
     *
     * @param id id de la oferta
     * @return vista de la lista de ofertas actualizada
     */
    @RequestMapping(value = "/offer/{id}/purchase", method = RequestMethod.GET)
    public String setPurchaseTrue(@PathVariable Long id) {
        invalidBuy = offersService.validatePurchase(id);
        if (!invalidBuy) {
            offersService.setOfferPurchase(true, id);
        }
        return "offer/searchList :: tableSearchedOffers";
    }

    /**
     * Método encargado de puner la oferta como destacada
     *
     * @param id id de la oferta
     * @return vista de la lista de ofertas actualizada
     */
    @RequestMapping(value = "/offer/{id}/feature", method = RequestMethod.GET)
    public String setFeatureTrue(@PathVariable Long id) {
        invalidFeature = offersService.validateFeature();
        if (!invalidFeature) {
            offersService.setOfferFeature(true, id);
        }
        return "offer/ownedList :: tableOwnedOffers";
    }

    /**
     * Método encargado de devolver la vista de ofertas compradas
     *
     * @param model     id de la oferta
     * @param principal principal
     * @return vista de la lista de ofertas compradas
     */
    @RequestMapping(value = "/offer/purchasedList", method = RequestMethod.GET)
    public String getPurchasedList(Model model, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        model.addAttribute("offersList", offersService.getOffersOfBuyer(user));
        return "offer/purchasedList";
    }

}
