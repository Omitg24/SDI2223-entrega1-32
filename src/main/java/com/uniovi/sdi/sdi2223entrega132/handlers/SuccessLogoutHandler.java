package com.uniovi.sdi.sdi2223entrega132.handlers;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SuccessLogoutHandler implements LogoutSuccessHandler {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        loggerService.log(LogMessage.Action.LOGOUT,messageSource.getMessage("msg.log.logout.user.success",null,request.getLocale()) + authentication.getName());
        response.sendRedirect("/login");
    }
}
