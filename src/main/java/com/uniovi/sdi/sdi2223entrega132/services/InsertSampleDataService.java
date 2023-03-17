package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;

@Service
public class InsertSampleDataService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private OffersService offersService;
    @Autowired
    private RolesService rolesService;

    @Autowired
    private ConversationService conversationService;

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

        User pepe = new User("pepe@email.com", "", "");
        pepe.setPassword("pepe");
        pepe.setRole(rolesService.getRoles()[0]);
        usersService.addUser(pepe);
        User pepe1 = new User("pepe1@email.com", "", "");
        pepe1.setPassword("pepe1");
        pepe1.setRole(rolesService.getRoles()[0]);
        usersService.addUser(pepe1);

        User jincho = new User("jincho@email.com", "kinkiyeroski", "");
        jincho.setPassword("jincho");
        jincho.setRole(rolesService.getRoles()[0]);
        usersService.addUser(jincho);

        Date date= new Date(2023,3,15);
        Offer offer = new Offer("Playeros","Están nuevos",date,15.0,admin);
        offersService.addOffer(offer);

        Offer offer2 = new Offer("Cartera","No robada",date,5.0,jincho);
        offersService.addOffer(offer2);

        Conversation c = new Conversation(offer2,pepe,new HashSet<>());

        conversationService.addConversationForOffer(c);

        Offer offer1 = new Offer("Mando Play","Están nuevos",date,40.0,user);
        offersService.addOffer(offer1);

    }
}