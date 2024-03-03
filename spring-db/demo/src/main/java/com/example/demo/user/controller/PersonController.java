package com.example.demo.user.controller;

import com.example.demo.user.domain.Person;
import com.example.demo.user.service.PersonRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonRedisService personRedisService;

    @GetMapping
    public List<Person> findAll(){
        return personRedisService.findAll();
    }

    @GetMapping("/{id}")
    public Person findOne(@PathVariable("id") String id){
        return personRedisService.findPerson(id);
    }

    @PostMapping
    public Person savePerson(@RequestBody  Person person){
        return personRedisService.savePerson(person);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable("id") String id){
        personRedisService.deletePerson(id);
    }

}
