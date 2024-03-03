package com.example.demo.game.controller;

import com.example.demo.game.domain.MyRacer;
import com.example.demo.game.domain.Racer;
import com.example.demo.game.repository.MyRacerRepository;
import com.example.demo.game.repository.RacerRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/myRacer")
public class MyRacerController {

    private final MyRacerRepository myRacerRepository;
    private final RacerRepository racerRepository;
    private final UserRepository userRepository;
    
    @PostMapping
    public Racer saveMyRacer(@RequestBody Map<String, Object> params, HttpSession session){
        String racername = (String) params.get("racername");
        Racer racer = racerRepository.findByName(racername);

        User user = (User) session.getAttribute("user");
        log.info("session user {}", user.getUserEmail());
        myRacerRepository.addMyRacer(user.getUserId(),racername);

        return racer;
    }

    @GetMapping
    public List<String> myRacerList(HttpSession session){
        User user = (User) session.getAttribute("user");
        int userId= user.getUserId();

        List<String> result = new ArrayList<>();
        User findUser = userRepository.findById(userId);

        List<MyRacer> myRacerList = findUser.getMyRacers();
        for (MyRacer myRacer : myRacerList) {
            result.add(myRacer.getRacer().getRacerName());
        }

        return result;
    }

    @GetMapping("/touch")
    public void touchMyRacerList(HttpSession session){
        User user = (User) session.getAttribute("user");
        int userId= user.getUserId();

        User findUser = userRepository.findById(userId);



        List<MyRacer> myRacerList = findUser.getMyRacers();
    }



}
