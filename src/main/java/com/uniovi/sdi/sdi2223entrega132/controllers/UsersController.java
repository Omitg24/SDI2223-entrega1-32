package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.RolesService;
import com.uniovi.sdi.sdi2223entrega132.services.SecurityService;
import com.uniovi.sdi.sdi2223entrega132.services.UsersService;
import com.uniovi.sdi.sdi2223entrega132.validators.SignUpFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

@Controller
public class UsersController {
    @Autowired
    private RolesService rolesService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private SignUpFormValidator signUpFormValidator;

    @RequestMapping("/user/list")
    public String getList(Model model, Pageable pageable, Principal principal, @RequestParam(value = "", required = false) String searchText) {
        String email = principal.getName();
        User user = usersService.getUserByEmail(email);
        Page<User> users = new PageImpl<User>(new LinkedList<User>());
        if (searchText != null && !searchText.isEmpty()) {
            users = usersService.searchByNameAndLastName(pageable, searchText, user);
        } else {
            users = usersService.getUsers(pageable);
        }
        model.addAttribute("usersList", users.getContent());
        model.addAttribute("page", users);
        return "user/list";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Validated User user, BindingResult result) {
        signUpFormValidator.validate(user, result);
        if (result.hasErrors()) {
            return "signup";
        }
        user.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user);
        securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
        return "redirect:home";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login/error", method = RequestMethod.GET)
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "login";
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User activeUser = usersService.getUserByEmail(email);
        model.addAttribute("ownedList", activeUser.getOwnOffers());
        return "home";
    }

    @RequestMapping(value = {"/default"}, method = RequestMethod.GET)
    public String userRedirect() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = usersService.getUserByEmail(email);
        if (user.getRole().equals(rolesService.getRoles()[1])) {
            return "redirect:user/list";
        }
        return "redirect:home";
    }

    @RequestMapping("/user/list/update")
    public String updateList(Pageable pageable, Model model) {
        model.addAttribute("usersList", usersService.getUsers(pageable));
        return "user/list :: tableUsers";
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public String deleteUsers(@RequestParam("users") Long[] users, HttpServletRequest request) {
        for(Long id : users){
            usersService.deleteUser(id);
        }
        return "redirect:/user/list";
    }
}