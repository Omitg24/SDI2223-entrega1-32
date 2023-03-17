package com.uniovi.sdi.sdi2223entrega132.controllers;

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
import java.util.LinkedList;

@Controller
public class OffersController {
    @Autowired
    private OffersService offersService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private AddOfferFormValidator addOfferFormValidator;

    /**
     * Método para obtener la vista de las ofertas que pueden ser compradas
     * @param model modelo
     * @param pageable pagina
     * @param searchText texto de busqueda
     * @return vista de las ofertas
     */
    @RequestMapping(value = "/offer/searchList", method = RequestMethod.GET)
    public String getSearchList(Model model, Pageable pageable,
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

    /**
     * Método para obtener la vista de las ofertas propias del usuario
     * @param model modelo
     * @param pageable pagina
     * @param principal objeto para obtener los datos del usuario autenticado
     * @return vista de las ofertas propias
     */
    @RequestMapping(value = "/offer/ownedList", method = RequestMethod.GET)
    public String getOwnedList(Model model,Pageable pageable, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        Page<Offer> offers = offersService.getOffersOfUser(pageable,user);
        model.addAttribute("offersList",offers.getContent());
        model.addAttribute("page", offers);
        return "offer/ownedList";
    }

    /**
     * Método para actualizar la lista de ofertas propias
     * @param model modelo
     * @param pageable pagina
     * @param principal objeto para obtener los datos del usuario autenticado
     * @return fragmento de las ofertas propias actualizado
     */
    @RequestMapping("/offer/ownedList/update")
    public String updateList(Model model, Pageable pageable, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        Page<Offer> offers = offersService.getOffersOfUser(pageable,user);
        model.addAttribute("offersList", offers.getContent());
        return "fragments/ownedOffers";

    }

    /**
     * Método para obtener la vista de añadir ofertas
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
     * @param offer datos oferta
     * @param principal principal
     * @param result result
     * @return vista de las ofertas si se añade o del formulario en caso de error
     */
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
