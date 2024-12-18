package com.glgl.e_com.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> items = new ArrayList<>();

    private double totalPrice;
    private Date date;

    public void addItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                if (product.getStock() < item.getQuantity() + quantity) {
                    throw new IllegalArgumentException("Not enough stock available");
                }
                item.setQuantity(item.getQuantity() + quantity);
                updateTotalPrice();
                updateDate();
                return;
            }
        }
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }
        this.items.add(new CartItem(product, quantity));
        updateTotalPrice();
        updateDate();
    }

    public void removeItem(Product product) {
        items.removeIf(item -> item.getProduct().equals(product));
        updateTotalPrice();
        updateDate();
    }

    public void clearItems() {
        items.clear();
        totalPrice = 0;
        updateDate();
    }

    public void updateTotalPrice() {
        this.totalPrice = items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void updateDate() {
        this.date = new Date();
    }

}
