package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

        User user01 = new User("user01@email.com", "", "");
        user01.setPassword("user01");
        user01.setRole(rolesService.getRoles()[0]);

        User user02 = new User("user02@email.com", "", "");
        user02.setPassword("user02");
        user02.setRole(rolesService.getRoles()[0]);

        User user03 = new User("user03@email.com", "", "");
        user03.setPassword("user03");
        user03.setRole(rolesService.getRoles()[0]);

        User user04 = new User("user04@email.com", "", "");
        user04.setPassword("user04");
        user04.setRole(rolesService.getRoles()[0]);
        user04.setAmount(0.00);

        User user05 = new User("user05@email.com", "", "");
        user05.setPassword("user05");
        user05.setRole(rolesService.getRoles()[0]);

        User user06 = new User("user06@email.com", "", "");
        user06.setPassword("user06");
        user06.setRole(rolesService.getRoles()[0]);

        User user07 = new User("user07@email.com", "", "");
        user07.setPassword("user07");
        user07.setRole(rolesService.getRoles()[0]);


        User user08 = new User("user08@email.com", "", "");
        user08.setPassword("user08");
        user08.setRole(rolesService.getRoles()[0]);

        User user09 = new User("user09@email.com", "", "");
        user09.setPassword("user09");
        user09.setRole(rolesService.getRoles()[0]);

        User user10 = new User("user10@email.com", "", "");
        user10.setPassword("user10");
        user10.setRole(rolesService.getRoles()[0]);

        User user11 = new User("user11@email.com", "", "");
        user11.setPassword("user11");
        user11.setRole(rolesService.getRoles()[0]);

        User user12 = new User("user12@email.com", "", "");
        user12.setPassword("user12");
        user12.setRole(rolesService.getRoles()[0]);

        User user13 = new User("user13@email.com", "", "");
        user13.setPassword("user13");
        user13.setRole(rolesService.getRoles()[0]);

        User user14 = new User("user14@email.com", "", "");
        user14.setPassword("user14");
        user14.setRole(rolesService.getRoles()[0]);

        User user15 = new User("user15@email.com", "", "");
        user15.setPassword("user15");
        user15.setRole(rolesService.getRoles()[0]);

        List<User> users = new LinkedList<>();

        usersService.addUser(admin);
        usersService.addUser(user01);
        users.add(user01);
        usersService.addUser(user02);
        users.add(user02);
        usersService.addUser(user03);
        users.add(user03);
        usersService.addUser(user04);
        users.add(user04);
        usersService.addUser(user05);
        users.add(user05);
        usersService.addUser(user06);
        users.add(user06);
        usersService.addUser(user07);
        users.add(user07);
        usersService.addUser(user08);
        users.add(user08);
        usersService.addUser(user09);
        users.add(user09);
        usersService.addUser(user10);
        users.add(user10);
        usersService.addUser(user11);
        users.add(user11);
        usersService.addUser(user12);
        users.add(user12);
        usersService.addUser(user13);
        users.add(user13);
        usersService.addUser(user14);
        users.add(user14);
        usersService.addUser(user15);
        users.add(user15);


        Random r = new Random();
        for(int i =0;i<140;i++) {
            int userIndex = i % 5;
            User user = users.get(userIndex);
            if(i==20) {
                offersService.addOffer(new Offer("Producto "+i,
                        "Descripción del producto "+i,new Date(),50.00,user));
            } else if(i==25) {
                offersService.addOffer(new Offer("Producto " + i,
                        "Descripción del producto " + i, new Date(), 1000.00, user));
            } else if(i==117) {
                offersService.addOffer(new Offer("Producto " + i,
                        "Descripción del producto " + i, new Date(), 69.69, user));
            } else if(i==130) {
                offersService.addOffer(new Offer("Producto " + i,
                        "Descripción del producto " + i, new Date(), 100.00, user));
            } else {
                offersService.addOffer(new Offer("Producto " + i,
                        "Descripción del producto " + i, new Date(), Math.round(1 + (200 - 1) * r.nextDouble()* 100.0) / 100.0, user));
            }
        }
    }
}