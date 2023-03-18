package com.uniovi.sdi.sdi2223entrega132;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.services.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@Component
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private LoggerService loggerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String params = request.getQueryString();
        if(params != null && params.equals("logout")){
            loggerService.log(LogMessage.Action.LOGOUT, "Logout for username: " + auth.getName());
        }
        loggerService.log(LogMessage.Action.PET, "Petition: " + request.getMethod()+ " for URL: " + request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String requestUrl = request.getRequestURI();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(requestUrl.contains("login")){
            if(requestUrl.contains("error")){
                loggerService.log(LogMessage.Action.LOGIN_ERR, "Log in error for username: " + ((UsernamePasswordAuthenticationToken) auth).getPrincipal().toString());
            }else{
                loggerService.log(LogMessage.Action.LOGIN_EX, "User: " +  auth.getName() + " logged in");
            }
        }
        if(requestUrl.contains("signup")){
            loggerService.log(LogMessage.Action.ALTA, "Petition: " + request.getMethod()+ " for URL: " + request.getRequestURI());
        }
    }
}
