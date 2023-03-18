package com.uniovi.sdi.sdi2223entrega132.controllers;

import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import com.uniovi.sdi.sdi2223entrega132.entities.User;
import com.uniovi.sdi.sdi2223entrega132.services.OffersService;
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

import java.security.Principal;
import java.util.LinkedList;

/**
 * Controlador de las peticiones relacionadas con la gestión de usuarios
 *
 * @author Omar Teixeira González, David Leszek Warzynski Abril e Israel Solís Iglesias
 * @version 12/03/2023
 */
@Controller
public class UsersController {
    @Autowired
    private RolesService rolesService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private OffersService offersService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private SignUpFormValidator signUpFormValidator;

    /**
     * Método para obtener la vista de todos los usuarios que figuran en el sistema
     *
     * @param model      modelo
     * @param pageable   página actual
     * @param principal  objeto para obtener los datos del usuario autenticado
     * @param searchText texto a buscar en la lista
     * @return vista de los usuarios
     */
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

    /**
     * Método encargado de validar los datos del usuario que se registra y crearlo
     *
     * @param user   datos del usuario
     * @param result resultado de la validación
     * @param model modelo
     * @return panel principal si los datos son correctos o del formulario sin son incorrectos
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@Validated User user, BindingResult result, Model model) {
        signUpFormValidator.validate(user, result);
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "signup";
        }
        user.setRole(rolesService.getRoles()[0]);
        usersService.addUser(user);
        securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
        return "redirect:home";
    }

    /**
     * Método para obtener la vista de registro para los usuarios
     *
     * @param model modelo
     * @return vista de registro
     */
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    /**
     * Método para obtener la vista de logeo para los usuarios
     *
     * @return vista de logeo
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * Método para cargar el mensaje de error en caso de logeo incorrecto,
     *
     * @param model modelo
     * @return vista de logeo
     */
    @RequestMapping(value = "/login/error", method = RequestMethod.GET)
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "login";
    }

    /**
     * Método para obtener la vista de home/panel principal
     *
     * @param model    model
     * @param pageable pagina actual
     * @return vista de home
     */
    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public String home(Model model, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = usersService.getUserByEmail(email);
        Page<Offer> offers = offersService.getOffersOfUser(pageable, user);
        model.addAttribute("offersList", offers.getContent());
        model.addAttribute("featuredList", offersService.getOffersFeatured());
        model.addAttribute("amount", user.getAmount());
        model.addAttribute("page", offers);
        return "home";
    }

    /**
     * Método para redirigir al usuario a una vista u otra en función de su rol, de esta manera:
     * - Si el usuario es administrador sera redirigido a la vista de usuarios
     * - Si el usuario no es administrador será redirigido al panel principal
     *
     * @return vista de listado de usuarios si es admin o panel principal si no lo es
     */
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

    /**
     * Método para actualizar la lista de usuarios
     *
     * @param pageable pagina
     * @param model    modelo
     * @return fragmento de la tabla de usuarios actualizado
     */
    @RequestMapping("/user/list/update")
    public String updateList(Pageable pageable, Model model) {
        model.addAttribute("usersList", usersService.getUsers(pageable));
        return "user/list :: tableUsers";
    }

    /**
     * Método para borrar usuarios de la lista
     *
     * @param users lista de usuarios a borrar
     * @return vista de listado de usuarios
     */
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public String deleteUsers(@RequestParam("users") Long[] users) {
        for (Long id : users) {
            usersService.deleteUser(id);
        }
        return "redirect:/user/list";
    }
}