package com.example.fluxdemo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Customer {

    @Id
    private Long id;
    private final String firstName;
    private final String lastName;
}