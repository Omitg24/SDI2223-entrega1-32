package com.uniovi.sdi.sdi2223entrega132.handlers;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FailureLoginHandler implements AuthenticationFailureHandler {

    //Inyectamos el servicio de loggin para logear las peticiones
    @Autowired
    private LoggerService loggerService;

    //Inyectamos un servicio para poder obtener mensajes en funcion del idioma
    @Autowired
    private MessageSource messageSource;

    /**
     * Metodo que se ejecutara cuando un usuario no haya iniciado sesion correctamente, quedara registrado en la base de datos
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        loggerService.log(LogMessage.Action.LOGIN_ERR,messageSource.getMessage("msg.log.login.user.failure",null,request.getLocale()) + exception.getMessage());
        response.sendRedirect("/login/error");
    }
}
