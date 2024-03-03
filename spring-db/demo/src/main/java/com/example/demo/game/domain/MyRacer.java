package com.example.demo.game.domain;

import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MyRacer", uniqueConstraints = {
        @UniqueConstraint(
                name = "USER_RACER_UNIQUE",
                columnNames= {"user_id", "racer_id"}
        )})
@Getter
@Setter
public class MyRacer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "myracer_id")
    private int myracerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "racer_id")
    private Racer racer;

    @Override
    public String toString() {
        return "MyRacer{" +
                "myracerId=" + myracerId +
                ", user=" + user.getUserId() +
                ", racer=" + racer.getRacerName() +
                '}';
    }
}