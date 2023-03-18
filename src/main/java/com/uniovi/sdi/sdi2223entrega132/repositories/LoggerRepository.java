package com.uniovi.sdi.sdi2223entrega132.repositories;

import com.uniovi.sdi.sdi2223entrega132.entities.LogMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggerRepository extends CrudRepository<LogMessage, Long> {

    @Query("SELECT m from LogMessage m ORDER BY m.date DESC")
    List<LogMessage> findAllOrdered();
}
