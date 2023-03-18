package com.uniovi.sdi.sdi2223entrega132.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class LogMessage {

    public enum Action {
        PET, ALTA, LOGIN_EX, LOGIN_ERR,LOGOUT;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    private Action action;

    private String message;

    public LogMessage(){

    }

    public LogMessage(Date date, Action action, String message) {
        this.date = date;
        this.action = action;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
