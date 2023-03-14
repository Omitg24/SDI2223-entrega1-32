package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import com.uniovi.sdi.sdi2223entrega132.entities.Message;
import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.repositories.ConversationRepository;
import com.uniovi.sdi.sdi2223entrega132.repositories.MessageRepository;
import com.uniovi.sdi.sdi2223entrega132.repositories.OffersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

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

    public List<Offer> getOffersOfUser(User user) {
        return offersRepository.findAllByOwner(user);
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

    public Optional<Conversation> getConversationOfUserAndOffer(User user, Offer offer){
        return conversationRepository.findByUserAndOffer(user.getId(),offer.getId());
    }

    public void addConversationForOffer(Conversation c) {
        conversationRepository.save(c);
    }

    public void addMessage(Message m) {
        messageRepository.save(m);
    }

    public Optional<Message> getMessage(Long id){
        return messageRepository.findById(id);
    }
}
