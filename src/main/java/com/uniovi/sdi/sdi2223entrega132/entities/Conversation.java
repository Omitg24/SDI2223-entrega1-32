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

    @ManyToOne
    @JoinColumn(name = "interested_id")
    private User interested;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    public Set<Message> messages;

    public Conversation() {
    }

    public Conversation(Offer offer,User interested, Set<Message> messages) {
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

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
