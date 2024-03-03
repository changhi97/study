package com.example.demo.user.repository;

import com.example.demo.user.domain.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRedisRepository extends CrudRepository<Person, String> {

}
