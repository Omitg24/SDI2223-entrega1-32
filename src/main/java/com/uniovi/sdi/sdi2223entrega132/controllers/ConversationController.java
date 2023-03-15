package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import com.uniovi.sdi.sdi2223entrega132.entities.Message;
import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.ConversationService;
import com.uniovi.sdi.sdi2223entrega132.services.OffersService;
import com.uniovi.sdi.sdi2223entrega132.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;

@Controller
public class ConversationController {

    @Autowired
    private OffersService offersService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/conversation/{id}", method = RequestMethod.GET)
    public String getConversation(@PathVariable Long id, Model model, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        Offer offerOfConversation = offersService.getOfferById(id);
        Optional<Conversation> optionalConversation = conversationService.getConversationOfUserAndOffer(user,offerOfConversation);
        Conversation c = optionalConversation.isEmpty()?null:optionalConversation.get();
        model.addAttribute("conversation",c);
        model.addAttribute("offer",offerOfConversation);
        return "conversation/conversation";
    }

    @RequestMapping(value = "/conversation/{id}", method = RequestMethod.POST)
    public String updateConversation(@PathVariable Long id, @ModelAttribute Message message, Model model, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        message.setDate(new Date());
        message.setOwner(user);


        Offer offerOfConversation = offersService.getOfferById(id);
        Optional<Conversation> optionalConversation = conversationService.getConversationOfUserAndOffer(user,offerOfConversation);
        Conversation c = optionalConversation.isEmpty()?new Conversation(offerOfConversation,user,new HashSet<>()):optionalConversation.get();
        conversationService.addConversationForOffer(c);
        message.setConversation(c);
        conversationService.addMessage(message);
        c.getMessages().add(message);

        model.addAttribute("conversation",c);
        model.addAttribute("offer",offerOfConversation);
        return "redirect:/conversation/" + id;
    }

    @RequestMapping("/conversation/list/update")
    public String updateList(Pageable pageable, Model model,Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        model.addAttribute("conversationList", conversationService.getConversationOfUser(pageable,user));
        return "fragments/tableConversations";
    }

    @RequestMapping("/conversation/list")
    public String getList(Model model, Pageable pageable, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        model.addAttribute("conversationList", conversationService.getConversationOfUser(pageable,user));
        return "conversation/list";
    }
}
