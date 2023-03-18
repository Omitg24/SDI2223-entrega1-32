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

    /**
     * Metodo que accede al repositorio para obtener las conversaciones de una oferta y un usuario
     * @param user
     * @param offer
     * @return
     */
    public Optional<Conversation> getConversationOfUserAndOffer(User user, Offer offer){
        return conversationRepository.findByUserAndOffer(user.getId(),offer.getId());
    }

    /**
     * Metodo que accede al repositorio para añadir una oferta
     * @param c
     */
    public void addConversationForOffer(Conversation c) {
        conversationRepository.save(c);
    }

    /**
     * Metodo que accede al repositorio para añadir un mensaje
     * @param m
     */
    public void addMessage(Message m) {
        messageRepository.save(m);
    }

    /**
     * Metodo que accede al repositorio para obtener las conversaciones de un usuario
     * @param pageable
     * @param user
     * @return
     */
    public Page<Conversation> getConversationOfUser(Pageable pageable, User user) {
        return conversationRepository.findByUser(pageable,user);
    }

    /**
     * Metodo que accede al repositorio para eliminar una conversacion por id
     * @param id
     */
    public void deleteConversation(Long id) {
        conversationRepository.deleteById(id);
    }
}
