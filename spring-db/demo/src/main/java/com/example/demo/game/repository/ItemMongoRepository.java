package com.example.demo.game.repository;

import com.example.demo.game.domain.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemMongoRepository extends MongoRepository<Item, String> {

    Item findByName(String name);

    List<Item> findByPriceGreaterThanEqual(int price);

    @Query("{'name' : 'defence', 'price':{$gte : :?0}}")
    List<Item> findByNameAndPrice(int price);

    @Query("{'$or': [{'name': 'defence'}, {'price': {$gte: :?0}}]}")
    List<Item> findByNameOrPrice(int price);
}
