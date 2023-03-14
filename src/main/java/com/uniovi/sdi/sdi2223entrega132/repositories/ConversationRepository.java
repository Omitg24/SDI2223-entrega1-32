package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConversationRepository extends CrudRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c WHERE (c.offer.id = ?2 AND c.interested.id = ?1)")
    Optional<Conversation> findByUserAndOffer(Long id, Long id1);
}
