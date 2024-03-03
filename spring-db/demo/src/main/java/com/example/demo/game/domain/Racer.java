package com.example.demo.game.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "Racer")
public class Racer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "racer_id")
    private int racerId;

    @Column(name = "racer_name", unique = true)
    private String racerName;

    @Column(name = "racer_price")
    private int racerPrice;
    protected Racer(){

    }
    @Builder
    public Racer(String racerName, int racerPrice){
        this.racerName = racerName;
        this.racerPrice =racerPrice;
    }
}



