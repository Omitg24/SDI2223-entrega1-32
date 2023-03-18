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

/**
 * Interceptor del log
 *
 * @author Israel Solís Iglesias
 * @version 18/03/2023
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    //Inyectamos el servicio de loggin para logear las peticiones
    @Autowired
    private LoggerService loggerService;

    //Inyectamos un servicio para poder obtener mensajes en funcion del idioma
    @Autowired
    private MessageSource messageSource;

    /**
     * Metodo que se ejecuta antes de procesar la solicitud HTTP
     *
     * @param request
     * @param response
     * @param handler
     * @return true
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //Logeamos cada peticion
        loggerService.log(LogMessage.Action.PET, messageSource.getMessage("msg.log.petition", null, request.getLocale()) + " " + request.getMethod() + " " + messageSource.getMessage("msg.log.forUrl", null, request.getLocale()) + " " + request.getRequestURI());
        return true;
    }

    /**
     * Metodo que se ejecuta después de que se haya procesado la solicitud
     * y se haya generado la vista, pero antes de enviar la respuesta al cliente.
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String requestUrl = request.getRequestURI();
        //Comprobamos si la petición es darse de alta
        if (requestUrl.contains("signup")) {
            loggerService.log(LogMessage.Action.ALTA, messageSource.getMessage("msg.log.petition", null, request.getLocale()) + " " + request.getMethod() + " " + messageSource.getMessage("msg.log.forUrl", null, request.getLocale()) + " " + request.getRequestURI());
        }
    }

}
