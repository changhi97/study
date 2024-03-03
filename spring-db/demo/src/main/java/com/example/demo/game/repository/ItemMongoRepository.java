package com.example.demo.game.repository;

import com.example.demo.game.domain.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemMongoRepository extends MongoRepository<Item,String> {

    Item findByName(String name);

    List<Item> findByPriceGreaterThanEqual(int price);

}
