package com.uniovi.sdi.sdi2223entrega132.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Offer {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Date uploadDate;
    private double price;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    public Offer(String title, String description, Date uploadDate, double price, User owner) {
        this.title = title;
        this.description = description;
        this.uploadDate = uploadDate;
        this.price = price;
        this.owner = owner;
    }

    public Offer() {

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
