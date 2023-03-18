package com.uniovi.sdi.sdi2223entrega132.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Conversation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    //Una conversacion tiene un interesado, forma parte de la clave primaria natural junto con offer
    @ManyToOne
    @JoinColumn(name = "interested_id")
    private User interested;

    //Una conversacion tiene varios mensajes
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    public List<Message> messages;

    public Conversation() {
    }

    public Conversation(Offer offer,User interested, List<Message> messages) {
        this.offer = offer;
        this.interested = interested;
        this.messages = messages;
    }

    public User getInterested() {
        return interested;
    }

    public void setInterested(User interested) {
        this.interested = interested;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
