package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import com.uniovi.sdi.sdi2223entrega132.entities.Message;
import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.repositories.ConversationRepository;
import com.uniovi.sdi.sdi2223entrega132.repositories.MessageRepository;
import com.uniovi.sdi.sdi2223entrega132.repositories.OffersRepository;
import com.uniovi.sdi.sdi2223entrega132.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class OffersService {
    @Autowired
    private OffersRepository offersRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Page<Offer> getAvailableOffers(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<Offer> offers = offersRepository.findAllAvailableOffers(pageable, email);
        return offers;
    }

    public Page<Offer> searchOffersByTitle(Pageable pageable, String searchText) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<Offer> offers = new PageImpl<Offer>(new LinkedList<Offer>());
        searchText = "%" + searchText + "%";
        offers = offersRepository.searchByTitle(pageable, email, searchText);
        return offers;
    }

    public void addOffer(Offer offer) {
        offersRepository.save(offer);
    }

    public Page<Offer> getOffersOfUser(Pageable pageable,User user) {
        return offersRepository.findAllByOwner(pageable, user);
    }

    public List<Offer> getOffersFeatured() {
        return offersRepository.findAllFeatured();
    }

    public Offer getOfferById(Long id) {
        return offersRepository.findById(id).get();
    }

    public void deleteOffer(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (getOfferById(id).getOwner().getEmail().equals(email)) {
            offersRepository.deleteById(id);
        }
    }


    public boolean validatePurchase(Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Offer offer = offersRepository.findById(id).get();
        User user =usersRepository.findByEmail(email);
        if(user.getAmount() >= offer.getPrice()){
            return false;
        }
        return true;
    }

    public boolean validateFeature(Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Offer offer = offersRepository.findById(id).get();
        User user =usersRepository.findByEmail(email);
        if(user.getAmount() >= 20){
            return false;
        }
        return true;
    }

    public void setOfferPurchase(boolean revised, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Offer offer = offersRepository.findById(id).get();
        User user =usersRepository.findByEmail(email);

        if(revised && !offer.isPurchase() && offer.getOwner().getEmail() != user.getEmail()) {
            if(user.getAmount() >= offer.getPrice()){
                //Se actualiza el comprador de la oferta
                offer.setBuyer(user);
                offer.setPurchase(true);
                offersRepository.updateOffer(true,offer.getBuyer(),id);
                //Se le suma el dinero al vendedor
                usersRepository.updateAmount(Math.round((offer.buyer.getAmount()+ offer.getPrice()) * 100.0) / 100.0,offer.buyer.getId());
                //Se le resta el dinero al comprador
                usersRepository.updateAmount(Math.round((user.getAmount()- offer.getPrice())* 100.0) / 100.0,user.getId());
            }
        }
    }

    public void setOfferFeature(boolean revised, Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Offer offer = offersRepository.findById(id).get();
        User user =usersRepository.findByEmail(email);

        if(revised && !offer.isFeatured() && offer.getOwner().getEmail() == user.getEmail()) {
            if(user.getAmount() >= 20.0){
                offer.setFeatured(true);
                offersRepository.updateFeatured(true,id);
                //Se le resta el dinero al propietario
                usersRepository.updateAmount(Math.round((user.getAmount()- 20.0) * 100.0) / 100.0,user.getId());
            }
        }
    }

    public List<Offer> getOffersOfBuyer(User user) {
        return offersRepository.findAllByBuyer(user);
    }

}
