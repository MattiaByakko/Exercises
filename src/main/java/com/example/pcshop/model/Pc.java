package com.example.pcshop.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("marca")
    private String marca;

    @JsonProperty("modello")
    private String modello;

    @JsonProperty("cpu")
    private String cpu;

    @JsonProperty("ram_gb")
    private int ramGb;

    @JsonProperty("storage_gb")
    private int storageGb;

    @JsonProperty("condizione")
    private String condizione;

    @JsonProperty("prezzo")
    private double prezzo;

    @JsonProperty("webcam")
    private boolean webcam;

    // Getters and setters omitted for brevity
}

