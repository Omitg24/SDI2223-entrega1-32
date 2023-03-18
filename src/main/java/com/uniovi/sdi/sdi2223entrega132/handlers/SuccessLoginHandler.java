package com.uniovi.sdi.sdi2223entrega132.handlers;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Manenjador de logeo correcto
 *
 * @author Israel Solís Iglesias
 * @version 18/03/2023
 */
@Component
public class SuccessLoginHandler implements AuthenticationSuccessHandler {

    //Inyectamos el servicio de loggin para logear las peticiones
    @Autowired
    private LoggerService loggerService;

    //Inyectamos un servicio para poder obtener mensajes en funcion del idioma
    @Autowired
    private MessageSource messageSource;

    /**
     * Metodo que se ejecutara cuando un usuario inicie sesion existosamente, quedando registrado en la base de datos
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        loggerService.log(LogMessage.Action.LOGIN_EX, messageSource.getMessage("msg.log.login.user.success", null, request.getLocale()) + authentication.getName());
        response.sendRedirect("/default");
    }
}
