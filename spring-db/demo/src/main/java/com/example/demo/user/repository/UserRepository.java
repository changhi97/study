package com.example.demo.user.repository;

import com.example.demo.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public User save(User user){
        em.persist(user);
        return user;
    }

    public User delete(int userId){
        User findUser= findById(userId);
        em.remove(findUser);
        return findUser;
    }

    public User findById(int userId){
        return em.find(User.class,userId);
    }

    public List<User> findAll(){
        return em.createQuery("select u from User u",User.class).getResultList();
    }
}
