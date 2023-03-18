package com.uniovi.sdi.sdi2223entrega132.interceptors;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        loggerService.log(LogMessage.Action.PET, messageSource.getMessage("msg.log.petition",null,request.getLocale()) +" "+ request.getMethod()+" "+ messageSource.getMessage("msg.log.forUrl",null,request.getLocale()) +" "+ request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception{
        String requestUrl = request.getRequestURI();
        if(requestUrl.contains("signup")){
            loggerService.log(LogMessage.Action.ALTA, messageSource.getMessage("msg.log.petition",null,request.getLocale()) +" "+ request.getMethod()+" "+ messageSource.getMessage("msg.log.forUrl",null,request.getLocale()) +" "+ request.getRequestURI());
        }
    }

}
