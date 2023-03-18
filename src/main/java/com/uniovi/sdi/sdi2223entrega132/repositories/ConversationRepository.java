package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de conversaciones
 *
 * @author Israel Solís Iglesias
 * @version 18/03/2023
 */
@Repository
public interface ConversationRepository extends CrudRepository<Conversation, Long> {
    //Obtiene la conversacion asociada a una oferta y al interesado en la oferta
    @Query("SELECT c FROM Conversation c WHERE (c.offer.id = ?2 AND (c.interested.id = ?1 OR c.offer.owner.id = ?1))")
    Optional<Conversation> findByUserAndOffer(Long id, Long id1);

    //Obtiene las conversaciones de un usuario
    @Query("SELECT c FROM Conversation c WHERE (c.offer.owner = ?1 OR c.interested = ?1)")
    Page<Conversation> findByUser(Pageable pageable, User user);
}
