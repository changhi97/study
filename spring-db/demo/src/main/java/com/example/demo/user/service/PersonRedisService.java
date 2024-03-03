package com.example.demo.user.service;

import com.example.demo.user.domain.Person;
import com.example.demo.user.repository.PersonRedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonRedisService {

    private final PersonRedisRepository personRedisRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static long ttlInSeconds = 30;
    //spring data redis에서는 ttl을 설정할수없다. 삽입만 redistemplate으로 한다
    public Person savePerson(Person person){
        Person savePerson = personRedisRepository.save(person);
//        String userJson = objectMapper.writeValueAsString(person);
//        redisTemplate.opsForValue().set(person.getId(),userJson,ttlInSeconds, TimeUnit.SECONDS);
        log.info("save Person {}", savePerson);

        return savePerson;
    }

    public Person findPerson(String id) {
        Optional<Person> findPerson = personRedisRepository.findById(id);
        return findPerson.orElse(null);
    }

    public List<Person> findAll() {
        List<Person> result = new ArrayList<>();
        Iterable<Person> all = personRedisRepository.findAll();
        for (Person person : all) {
            result.add(person);
        }
        log.info("findAll Person {}", result);
        return result;
    }

    public void deletePerson(String id) {
        log.info("delete before size {}", personRedisRepository.count());
        personRedisRepository.deleteById(id);
        log.info("delete after size {}", personRedisRepository.count());
    }

    //@Scheduled(cron = "* * * * * *")
//    @Scheduled(fixedDelay = 3000)
//    public void deleteExpiredData() {
//        // Redis의 모든 키를 가져옵니다.
//        Set<String> keys = redisTemplate.keys("*");
//
//        // 가져온 모든 키에 대해 반복하면서 TTL이 0 이하인 경우 삭제합니다.
//        for (String key : keys) {
//            Long ttl = redisTemplate.getExpire(key);
//            if (ttl != null && ttl <= 0) {
//                // TTL이 0 이하인 경우 해당 키의 데이터를 삭제합니다.
//                redisTemplate.delete(key);
//                log.info("delete key {}", key);
//            }
//        }
//    }

}
