package com.example.pcshop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private final LocalDateTime orderDate;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user; // Sostituisci con Client se la tua entità si chiama così

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItem> items = new ArrayList<>();

        public Order() {
            this.orderDate = LocalDateTime.now();
        }

        public Order(User user) {
            this.user = user;
            this.orderDate = LocalDateTime.now();
        }

        // Getters & Setters

        public void addItem(OrderItem item) {
            items.add(item);
            item.setOrder(this);
        }

        public void removeItem(OrderItem item) {
            items.remove(item);
            item.setOrder(null);
        }

        // Altri getter e setter

        public Long getId() {
            return id;
        }

        public LocalDateTime getOrderDate() {
            return orderDate;
        }

        public User getUser() {
            return user;
        }

        public List<OrderItem> getItems() {
            return items;
        }

        public void setUser(User user) {
            this.user = user;
        }
}
