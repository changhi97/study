package com.example.demo.game.service;

import com.example.demo.game.domain.Item;
import com.example.demo.game.repository.ItemMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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

    public List<Item> findByNameAndPrice(int price){
        return itemMongoRepository.findByNameAndPrice(price);
    }

    public List<Item> findByNameOrPrice(int price){
        return itemMongoRepository.findByNameOrPrice(price);
    }
}
