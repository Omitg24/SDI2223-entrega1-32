package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import com.uniovi.sdi.sdi2223entrega132.entities.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggerRepository extends CrudRepository<LogMessage, Long> {

    //Obtiene todos los logs ordenados por fecha
    @Query("SELECT m from LogMessage m ORDER BY m.date DESC")
    List<LogMessage> findAllOrdered();

    //Obtiene todos los logs en funcion del parametro de busqueda
    @Query("SELECT m from LogMessage m WHERE LOWER(m.action) like LOWER(?1) ORDER BY m.date DESC")
    List<LogMessage> findAllByType(String searchText);

}
