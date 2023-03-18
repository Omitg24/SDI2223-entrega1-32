package com.uniovi.sdi.sdi2223entrega132.services;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.repositories.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoggerService {

    @Autowired
    private LoggerRepository loggerRepository;

    public void log(LogMessage.Action action,String message){
        loggerRepository.save(new LogMessage(new Date(),action,message));
    }

    public List<LogMessage> getLogs() {
        return loggerRepository.findAllOrdered();
    }

    public List<LogMessage> searchLogsByType(String searchText) {
        return loggerRepository.findAllByType(searchText);
    }

    public void deleteLogs() {
        loggerRepository.deleteAll();
    }
}
