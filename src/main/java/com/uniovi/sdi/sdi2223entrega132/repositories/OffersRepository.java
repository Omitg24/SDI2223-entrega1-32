package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OffersRepository extends CrudRepository<Offer, Long> {

    @Query("SELECT o from Offer o WHERE o.owner = ?1 ORDER BY o.uploadDate DESC")
    List<Offer> findAllByOwner(User user);

    @Query("SELECT o from Offer o WHERE o.owner.email <> ?1 AND LOWER(o.title) like LOWER(?2) ORDER BY o.uploadDate DESC")
    Page<Offer> searchByTitle(Pageable pageable, String email, String title);

    @Query("SELECT o from Offer o WHERE o.owner.email <> ?1 ORDER BY o.uploadDate DESC")
    Page<Offer> findAllAvailableOffers(Pageable pageable, String email);


    @Modifying
    @Transactional
    @Query("UPDATE Offer SET purchase = ?1 , buyer = ?2 WHERE id = ?2")
    void updateOffer(Boolean purchase, User buyer, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Offer SET featured = ?1 WHERE id = ?2")
    void updateFeatured(Boolean featured, Long id);



    @Query("SELECT o from Offer o WHERE o.buyer = ?1 ORDER BY o.uploadDate DESC")
    List<Offer> findAllByBuyer(User user);

    @Query("SELECT o from Offer o WHERE o.featured = true ORDER BY o.uploadDate DESC")
    List<Offer> findAllFeatured();
}
