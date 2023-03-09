package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffersRepository extends CrudRepository<Offer,Long> {
}
