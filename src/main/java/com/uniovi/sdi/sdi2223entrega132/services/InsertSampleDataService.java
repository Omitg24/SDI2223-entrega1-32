package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InsertSampleDataService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RolesService rolesService;

    @PostConstruct
    public void init() {
        User admin = new User("admin@email.com", "", "");
        admin.setPassword("admin");
        admin.setRole(rolesService.getRoles()[1]);
        usersService.addUser(admin);

        User admin1 = new User("parlita@email.com", "", "");
        admin1.setPassword("parlita");
        admin1.setRole(rolesService.getRoles()[1]);
        usersService.addUser(admin1);

        User pepe = new User("pepe@email.com", "", "");
        pepe.setPassword("pepe");
        pepe.setRole(rolesService.getRoles()[0]);
        usersService.addUser(pepe);

        User jincho = new User("jincho@email.com", "kinkiyeroski", "");
        jincho.setPassword("jincho");
        jincho.setRole(rolesService.getRoles()[0]);
        usersService.addUser(jincho);
    }
}