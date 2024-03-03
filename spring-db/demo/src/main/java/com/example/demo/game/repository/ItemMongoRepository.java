package com.example.demo.game.repository;

import com.example.demo.game.domain.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ItemMongoRepository extends MongoRepository<Item, String> {

    Item findByName(String name);

    List<Item> findByPriceGreaterThanEqual(int price);

    //db.Item.find( { "name": "defence", "price": { $gte: 1000 } } )
    @Query("{'name' : 'defence', 'price':{$gte : :price}}")
    List<Item> findExpensiveDefence(@Param("price") int price);

    //db.Item.find({ $or : [ {"name" : "defence"}, {"price" : {$gte : 1500}} ]})
    @Query("{'$or': [{'name': 'defence'}, {'price': {$gte: :price}}]}")
    List<Item> findDefenceOther(@Param("price") int price);

}
