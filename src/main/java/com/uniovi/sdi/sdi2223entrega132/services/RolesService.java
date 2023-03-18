package com.uniovi.sdi.sdi2223entrega132.services;

import org.springframework.stereotype.Service;

/**
 * Servicio que contiene los roles de usuario en la aplicación
 *
 * @author Omar Teixeira González
 * @version 10/03/2023
 */
@Service
public class RolesService {
    String[] roles = {"ROLE_USER", "ROLE_ADMIN"};

    public String[] getRoles() {
        return roles;
    }
}
