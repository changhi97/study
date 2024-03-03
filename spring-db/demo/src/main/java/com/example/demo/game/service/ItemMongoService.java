package com.example.demo.game.service;

import com.example.demo.game.domain.Item;
import com.example.demo.game.repository.ItemMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemMongoService {
    private final ItemMongoRepository itemMongoRepository;

    public Item save(Item item){
        return itemMongoRepository.save(item);
    }

    public Item findByName(String name){
        return itemMongoRepository.findByName(name);
    }

    public List<Item> findAll(){
        return itemMongoRepository.findAll();
    }

    public List<Item> findByPriceGreaterThanEqual(int price){
        return itemMongoRepository.findByPriceGreaterThanEqual(price);
    }
}
