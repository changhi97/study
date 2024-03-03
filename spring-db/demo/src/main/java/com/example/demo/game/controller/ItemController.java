package com.example.demo.game.controller;

import com.example.demo.game.domain.Item;
import com.example.demo.game.service.ItemMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemMongoService itemMongoService;

    @PostMapping
    public Item saveItem(@RequestBody Item item){
        return itemMongoService.save(item);
    }

    @GetMapping("/name/{name}")
    public Item findByName(@PathVariable("name") String name){
        return itemMongoService.findByName(name);
    }

    @GetMapping
    public List<Item> findAll(){
        return itemMongoService.findAll();
    }

    @GetMapping("/price/{price}")
    public List<Item> findByPriceGreaterThanEqual(@PathVariable("price") int price){
        return itemMongoService.findByPriceGreaterThanEqual(price);
    }
}
