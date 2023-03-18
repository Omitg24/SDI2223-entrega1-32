package com.uniovi.sdi.sdi2223entrega132.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador de las peticiones de Home
 *
 * @author Omar Teixeira González
 * @version 11/03/2023
 */
@Controller
public class HomeController {
    /**
     * Método que obtiene la vista de la página inicial
     *
     * @return vista a la página principal
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
