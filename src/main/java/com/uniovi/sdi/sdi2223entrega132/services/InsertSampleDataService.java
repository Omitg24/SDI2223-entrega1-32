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

//    @PostConstruct
//    public void init() {
//        User admin = new User("admin@email.com", "", "");
//        admin.setPassword("admin");
//        admin.setRole(rolesService.getRoles()[1]);
//
//        usersService.addUser(admin);
//    }
}