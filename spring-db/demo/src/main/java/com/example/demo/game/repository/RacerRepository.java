package com.example.demo.game.repository;

import com.example.demo.game.domain.Racer;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@RequiredArgsConstructor
public class RacerRepository {

    private final EntityManager em;

    public Racer save(Racer racer){
        em.persist(racer);
        return racer;
    }

    public Racer findByName(String racername){
        return em.createQuery("select r from Racer r where r.racerName = :racername",Racer.class)
                .setParameter("racername",racername)
                .getSingleResult();
    }
}
