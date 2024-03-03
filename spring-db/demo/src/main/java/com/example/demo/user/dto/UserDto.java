package com.example.demo.user.dto;

import ch.qos.logback.core.net.server.ServerListener;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto implements Serializable {
    private int userId;
    private String userEmail;
    private String password;
    private List<String> myRacer = new ArrayList<>();

    @Builder
    public UserDto(int userId, String userEmail, String password) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", password='" + password + '\'' +
                ", myRacer=" + myRacer +
                '}';
    }
}
