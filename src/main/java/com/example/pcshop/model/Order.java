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
        @JoinColumn(name = "client_id")
        private Cliente cliente;

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItem> items = new ArrayList<>();

        public Order() {
            this.orderDate = LocalDateTime.now();
        }

        public Order(Cliente cliente) {
            this.cliente = cliente;
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


        public Long getId() {
            return id;
        }

        public LocalDateTime getOrderDate() {
            return orderDate;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public List<OrderItem> getItems() {
            return items;
        }

        public void setUser(User user) {
            this.cliente = cliente;
        }
}
