package com.example.demo.user.controller;

import com.example.demo.user.domain.Person;
import com.example.demo.user.domain.User;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.repository.PersonRedisRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.PersonRedisService;
import com.example.demo.user.service.UserRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserRedisService userRedisService;
    private final PersonRedisService personRedisService;

    @GetMapping("/{userId}")
    public UserDto findUser(@PathVariable("userId") int userId, HttpSession session){
        User findUser = userRepository.findById(userId);
        session.setAttribute("user", findUser);
        return toDto(findUser);
    }

    @PostMapping
    public void saveUser(@RequestBody UserDto userDto){
        User user = User
                .builder()
                .userEmail(userDto.getUserEmail())
                .password(userDto.getPassword())
                .build();
        userRepository.save(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") int userId){
        User user = userRepository.findById(userId);
        userRepository.delete(user.getUserId());
    }

    @GetMapping
    public List<UserDto> findAllUser(){
        List<UserDto> result = new ArrayList<>();
        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            result.add(toDto(user));
        }
        return result;
    }

    @GetMapping("/redis/{userId}")
    public UserDto getRedisUser(@PathVariable("userId") int userId) throws JsonProcessingException {
        return userRedisService.getUser(userId);
    }

    @PostMapping("/redis")
    public void setRedisUser(@RequestBody UserDto user) throws JsonProcessingException {
        log.info("redis user {}", user);
        userRedisService.saveUser(user);
    }
//
//    @PostMapping("/person")
//    public Person setRedisPerson(@RequestBody Person person) throws JsonProcessingException {
//        Person savePerson = personRedisService.savePerson(person);
//        return savePerson;
//    }
//
//    @GetMapping("/person")
//    public List<Person> getPersonList() throws JsonProcessingException {
//        log.info("redis person List");
//        return personRedisService.findAll();
//    }

    public UserDto toDto(User user){
        return UserDto
                .builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .password(user.getPassword())
                .build();
    }

}
