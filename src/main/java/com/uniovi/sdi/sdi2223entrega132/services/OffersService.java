package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.repositories.OffersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OffersService {
    @Autowired
    private OffersRepository offersRepository;

    public void addOffer(Offer offer) {
        offersRepository.save(offer);
    }

}
