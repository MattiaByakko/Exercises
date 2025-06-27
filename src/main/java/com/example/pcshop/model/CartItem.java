package com.example.pcshop.model;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
 public class CartItem {

     @Id @GeneratedValue
     private Long id;

     @ManyToOne
     private Pc product;

     private int quantity;
}
