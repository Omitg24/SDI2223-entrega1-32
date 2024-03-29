package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.repositories.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Servicio que contiene capa de negocio del logger
 *
 * @author Israel Solís Iglesias
 * @version 18/03/2023
 */
@Service
public class LoggerService {

    @Autowired
    private LoggerRepository loggerRepository;

    /**
     * Metodo que accede al repositorio para almacenar los logs
     *
     * @param action acción realizada
     * @param message mensaje
     */
    public void log(LogMessage.Action action, String message) {
        loggerRepository.save(new LogMessage(new Date(), action, message));
    }

    /**
     * Metodo que accede al repositorio para obtener los logs
     *
     * @return logs
     */
    public List<LogMessage> getLogs() {
        return loggerRepository.findAllOrdered();
    }

    /**
     * Metodo que accede al repositorio para obtener los logs por tipo
     *
     * @return logs por tipo
     */
    public List<LogMessage> searchLogsByType(String searchText) {
        return loggerRepository.findAllByType(searchText);
    }

    /**
     * Metodo que accede al repositorio para eliminar los logs
     */
    public void deleteLogs() {
        loggerRepository.deleteAll();
    }
}
