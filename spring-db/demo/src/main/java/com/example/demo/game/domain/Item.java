package com.example.demo.game.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.annotation.Documented;

@Document(collection = "Item")
@Getter
@Setter
public class Item {
    private String id;
    private String name;
    private int price;
    private int durability;
}
