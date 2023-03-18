package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controlador de las peticiones relacionadas con el log
 *
 * @author Israel Solís Iglesias
 * @version 18/03/2023
 */
@Controller
public class LogController {

    @Autowired
    private LoggerService loggerService;

    /**
     * Metodo que obtiene los logs de la base de datos y si hay una busqueda obtiene los logs en funcion del texto
     * y devuelve una vista que se encargara de mostrarlos
     *
     * @param model modelo
     * @param searchText texto a buscar
     * @return log/list
     */
    @RequestMapping("/log")
    public String getLogs(Model model, @RequestParam(value = "", required = false) String searchText) {
        List<LogMessage> logMessages;
        //Si no hay ningun filtro
        if (searchText != null && !searchText.isEmpty()) {
            model.addAttribute("searchText", searchText);
            logMessages = loggerService.searchLogsByType(searchText);
        } else {
            logMessages = loggerService.getLogs();
        }
        model.addAttribute("logMessages", logMessages);
        return "log/list";
    }

    /**
     * Metodo que elimina todos los logs de la base de datos
     *
     * @param model modelo
     * @return log/list
     */
    @RequestMapping("/log/delete")
    public String deleteLogs(Model model) {
        loggerService.deleteLogs();
        return "log/list";
    }

    /**
     * Metodo que actualiza la lista de logs
     *
     * @param model
     * @param searchText
     * @return fragments/tableLogs
     */
    @RequestMapping("/log/update")
    public String updateLogs(Model model, @RequestParam(value = "", required = false) String searchText) {
        List<LogMessage> logMessages = loggerService.getLogs();
        model.addAttribute("logMessages", logMessages);
        return "fragments/tableLogs";
    }
}
