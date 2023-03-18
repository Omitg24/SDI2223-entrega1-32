package com.uniovi.sdi.sdi2223entrega132.entities;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Entidad de la oferta
 *
 * @author David Leszek Warzynski Abril, Israel Solís Iglesias, Álvaro Davila Sampedro y Omar Teixeira
 * @version 17/03/2023
 */
@Entity
@Table(name = "offer")
public class Offer {
    @Id
    @GeneratedValue
    public Long id;
    public String title;
    public String description;
    public Date uploadDate;
    public double price;
    public boolean purchase;
    public boolean featured = false;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    public User owner;
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    public User buyer;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    public List<Conversation> conversations;

    public boolean hasPicture;

    @Transient
    public MultipartFile picture;

    public Offer() { }

    public Offer(String title, String description, Date uploadDate, double price, User owner) {
        this.title = title;
        this.description = description;
        this.uploadDate = uploadDate;
        this.price = price;
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", uploadDate=" + uploadDate +
                ", price=" + price +
                '}';
    }
}
