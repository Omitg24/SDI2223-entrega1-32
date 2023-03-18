package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LogController {

    @Autowired
    private LoggerService loggerService;

    @RequestMapping("/log")
    public String getLogs(Model model, @RequestParam(value = "", required = false) String searchText) {
        List<LogMessage> logMessages;
        if (searchText != null && !searchText.isEmpty()) {
            model.addAttribute("searchText", searchText);
            logMessages = loggerService.searchLogsByType(searchText);
        } else {
            logMessages = loggerService.getLogs();
        }
        model.addAttribute("logMessages", logMessages);
        return "log/list";
    }

    @RequestMapping("/log/delete")
    public String deleteLogs(Model model) {
        loggerService.deleteLogs();
        return "log/list";
    }

    @RequestMapping("/log/update")
    public String updateLogs(Model model, @RequestParam(value = "", required = false) String searchText) {
        List<LogMessage> logMessages = loggerService.getLogs();
        model.addAttribute("logMessages", logMessages);
        return "fragments/tableLogs";
    }
}
