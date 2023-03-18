package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.Message;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    //Obtiene todos los mensajes de un usuario dado
    @Query("SELECT m from Message m WHERE m.owner = ?1 ORDER BY m.date DESC")
    List<Message> findAllByOwner(User user);
}
