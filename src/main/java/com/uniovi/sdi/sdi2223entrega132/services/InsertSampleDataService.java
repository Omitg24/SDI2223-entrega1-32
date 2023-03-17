package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
public class InsertSampleDataService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private OffersService offersService;
    @Autowired
    private RolesService rolesService;

    @PostConstruct
   public void init() {
        User admin = new User("admin@email.com", "", "");
       admin.setPassword("admin");
       admin.setRole(rolesService.getRoles()[1]);

       usersService.addUser(admin);

        User user = new User("user@email.com", "", "");
        user.setPassword("123456");
        user.setRole(rolesService.getRoles()[0]);
        user.setAmount(1000.0);
        usersService.addUser(user);

        Date date= new Date(2023,3,15);
        Offer offer = new Offer("Playeros","Están nuevos",date,15.0,admin);
        offersService.addOffer(offer);

        Offer offer1 = new Offer("Mando Play","Están nuevos",date,40.0,user);
        offersService.addOffer(offer1);

    }
}