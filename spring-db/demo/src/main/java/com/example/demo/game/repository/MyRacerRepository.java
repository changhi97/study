package com.example.demo.game.repository;

import com.example.demo.game.domain.MyRacer;
import com.example.demo.game.domain.Racer;
import com.example.demo.user.domain.User;
import com.example.demo.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class MyRacerRepository {

    private final EntityManager em;
    private final RacerRepository racerRepository;
    private final UserRepository userRepository;

    public MyRacer save(MyRacer myRacer){
        em.persist(myRacer);
        return myRacer;
    }

    public List<MyRacer> findAll(int userId){
        return em.createQuery("select mr from MyRacer mr where mr.user.userId=:userId", MyRacer.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public MyRacer addMyRacer(int userId, String racername){
        User user = userRepository.findById(userId);
        Racer racer = racerRepository.findByName(racername);
        MyRacer myRacer = new MyRacer();
        myRacer.setUser(user);
        myRacer.setRacer(racer);

        save(myRacer);
        user.addMyRacer(myRacer);

        return myRacer;
    }


}
