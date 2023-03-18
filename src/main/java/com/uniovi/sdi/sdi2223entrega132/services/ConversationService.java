package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import com.uniovi.sdi.sdi2223entrega132.entities.Message;
import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.repositories.ConversationRepository;
import com.uniovi.sdi.sdi2223entrega132.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConversationService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public Optional<Conversation> getConversationOfUserAndOffer(User user, Offer offer){
        return conversationRepository.findByUserAndOffer(user.getId(),offer.getId());
    }

    public void addConversationForOffer(Conversation c) {
        conversationRepository.save(c);
    }

    public void addMessage(Message m) {
        messageRepository.save(m);
    }

    public Page<Conversation> getConversationOfUser(Pageable pageable, User user) {
        return conversationRepository.findByUser(pageable,user);
    }

    public void deleteConversation(Long id) {
        conversationRepository.deleteById(id);
    }
}
