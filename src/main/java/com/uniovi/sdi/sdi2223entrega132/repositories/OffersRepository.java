package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffersRepository extends CrudRepository<Offer,Long> {

    @Query("SELECT o from Offer o WHERE o.owner = ?1 ORDER BY o.uploadDate DESC")
    List<Offer> findAllByOwner(User user);
}
