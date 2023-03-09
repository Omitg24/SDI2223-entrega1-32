package com.uniovi.sdi.sdi2223entrega132.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;
    private String lastName;
    private Double amount = 100.0;
    private String password;
    private String role;

    @Transient
    private String passwordConfirm;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Offer> ownOffers = new HashSet<Offer>();

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private Set<Offer> purchasedOffers = new HashSet<Offer>();

    public User(String email, String name, String lastName) {
        super();
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }

    public User() {

    }
}
