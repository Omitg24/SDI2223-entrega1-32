package com.uniovi.sdi.sdi2223entrega132.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad del Usuario
 *
 * @author Omar Teixeira González y David Leszek Warzynski Abril
 * @version 12/03/2023
 */
@Entity
@Table(name = "user")
public class User {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private final Set<Offer> ownOffers = new HashSet<Offer>();
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private final Set<Offer> purchasedOffers = new HashSet<Offer>();
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
    private Set<Message> messages = new HashSet<Message>();

    public User() {
    }

    public User(String email, String name, String lastName) {
        super();
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Set<Offer> getOwnOffers() {
        return new HashSet<>(ownOffers);
    }

    public Set<Offer> getPurchasedOffers() {
        return new HashSet<>(purchasedOffers);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", amount=" + amount +
                '}';
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

}
