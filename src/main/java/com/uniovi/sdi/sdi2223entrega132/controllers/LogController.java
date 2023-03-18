package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.LinkedList;
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
