package com.example.demo.user.domain;

import com.example.demo.game.domain.MyRacer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue
    private int userId;

    @Column(unique = true)
    private String userEmail;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyRacer> myRacers = new ArrayList<>();

    public void addMyRacer(MyRacer myRacer) {
        myRacer.setUser(this);
        this.myRacers.add(myRacer);
    }

    //===연관관계 편의 메서드 예시===//
//    public void addChildCategory(Category child){
//        this.child.add(child);
//        child.setParent(this);
//    }


    @Builder
    public User(int userId, String userEmail, String password) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.password = password;
    }
}
