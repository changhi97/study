package com.example.demo.config;

import com.example.demo.game.domain.Racer;
import com.example.demo.game.repository.RacerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Init {

    private final RacerRepository racerRepository;

    @PostConstruct
    public void initRacer(){
        Racer racer1 = new Racer("강아지",5000);
        Racer racer2 = new Racer("고양이",5000);

        racerRepository.save(racer1);
        racerRepository.save(racer2);
    }

}
