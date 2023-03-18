package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.Conversation;
import com.uniovi.sdi.sdi2223entrega132.entities.Message;
import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.ConversationService;
import com.uniovi.sdi.sdi2223entrega132.services.OffersService;
import com.uniovi.sdi.sdi2223entrega132.services.UsersService;
import com.uniovi.sdi.sdi2223entrega132.validators.SendMessageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class ConversationController {

    @Autowired
    private OffersService offersService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private SendMessageValidator sendMessageValidator;

    /**
     * Metodo que obtiene la conversacion dada la oferta y el interesado por la oferta
     * @param offerId el identificador de la oferta
     * @param interestedId el identificador del interesado
     * @param model
     * @return la vista de la conversacion
     */
    @RequestMapping(value = "/conversation/{offerId}/{interestedId}", method = RequestMethod.GET)
    public String getConversation(@PathVariable Long offerId,@PathVariable Long interestedId, Model model) {
        prepareConversation(offerId,interestedId,model);
        model.addAttribute("message", new Message());
        return "conversation/conversation";
    }

    /**
     * Metodo que gestiona el envio de mensajes y persiste todas las entidades relacionadas.
     * @param offerId el identificador de la oferta de la conversacion
     * @param interestedId el identificador del interesado
     * @param message el mensaje enviado en la vista conversation/conversation
     * @param model
     * @param principal
     * @param result
     * @return
     */
    @RequestMapping(value = "/conversation/{offerId}/{interestedId}", method = RequestMethod.POST)
    public String updateConversation(@PathVariable Long offerId,@PathVariable Long interestedId,@ModelAttribute @Validated Message message, Model model, Principal principal, BindingResult result) {
        //Validamos que el mensaje no este vacio
        sendMessageValidator.validate(message,result);
        if (result.hasErrors()) {
            prepareConversation(offerId,interestedId, model);
            return "conversation/conversation";
        }
        // Obtenemos el usuario a partir del correo
        String email = principal.getName();
        User messageOwner = usersService.getUserByEmail(email);
        //Añadimos los campos fecha y dueño manualmente al mensaje
        message.setDate(new Date());
        message.setOwner(messageOwner);
        //Obtenemos o creamos la conversación en función de si existe o no
        Offer offerOfConversation = offersService.getOfferById(offerId);
        User interestedUser = usersService.getUser(interestedId);
        Optional<Conversation> optionalConversation = conversationService.getConversationOfUserAndOffer(interestedUser,offerOfConversation);
        Conversation c = optionalConversation.isEmpty()?new Conversation(offerOfConversation,interestedUser,new ArrayList<>()):optionalConversation.get();
        //Añadimos/actualizamos los datos en la base de datos
        conversationService.addConversationForOffer(c);
        message.setConversation(c);
        conversationService.addMessage(message);
        //Añadimos el mensaje enviado en el post a la conversación
        c.getMessages().add(message);

        model.addAttribute("conversation",c);
        model.addAttribute("offer",offerOfConversation);
        return "redirect:/conversation/"+offerId+"/"+interestedId;
    }

    /**
     * Metodo que actualiza la lista de conversaciones
     * @param pageable
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/conversation/list/update")
    public String updateList(Pageable pageable, Model model,Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        Page<Conversation> conversations = conversationService.getConversationOfUser(pageable,user);
        model.addAttribute("conversationList", conversations.getContent());
        model.addAttribute("page", conversations);
        return "fragments/tableConversations";
    }

    /**
     * Metodo que obtiene la lista de conversaciones del usuario autenticado
     * @param model
     * @param pageable
     * @param principal
     * @return
     */
    @RequestMapping("/conversation/list")
    public String getList(Model model, Pageable pageable, Principal principal) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        //Obtenemos las conversaciones del usuario
        Page<Conversation> conversations = conversationService.getConversationOfUser(pageable,user);
        model.addAttribute("conversationList", conversations.getContent());
        model.addAttribute("page", conversations);
        return "conversation/list";
    }

    /**
     * Metodo que elimina una oferta dado un identificador y devuelve la vista de la lista de conversaciones
     * @param id
     * @return
     */
    @RequestMapping(value = "/conversation/delete/{id}", method = RequestMethod.GET)
    public String deleteOffer(@PathVariable Long id) {
        conversationService.deleteConversation(id);
        return "redirect:/conversation/list";
    }

    /**
     * Metodo que obtiene la conversación del usuario actual para el id de la oferta pasado por parametro y añade
     * al modelo dicha conversación y la oferta
     * @param offerId
     * @param interestedId
     * @param model
     */

    private void prepareConversation(Long offerId,Long interestedId, Model model) {
        User interestedUser = usersService.getUser(interestedId);
        Offer offerOfConversation = offersService.getOfferById(offerId);
        Optional<Conversation> optionalConversation = conversationService.getConversationOfUserAndOffer(interestedUser,offerOfConversation);
        Conversation c = optionalConversation.isEmpty()?null:optionalConversation.get();
        model.addAttribute("conversation",c);
        model.addAttribute("offer",offerOfConversation);
        model.addAttribute("interestedUser",interestedUser);
    }
}
