package com.uniovi.sdi.sdi2223entrega132.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Conversation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    public Set<Message> messages;

    public Conversation() {
    }

    public Conversation(Offer offer, Set<Message> messages) {
        this.offer = offer;
        this.messages = messages;
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

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
