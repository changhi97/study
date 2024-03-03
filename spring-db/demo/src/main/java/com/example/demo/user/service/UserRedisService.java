package com.example.demo.user.service;

import com.example.demo.user.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String USER_KEY = "user:";
    public void saveUser(UserDto user) throws JsonProcessingException {
        String userJson = objectMapper.writeValueAsString(user);
        log.info("userJson {}", userJson);
        redisTemplate.opsForValue().set(USER_KEY+user.getUserId(),userJson);
    }

    public UserDto getUser(int userId) throws JsonProcessingException {
        String userJson = redisTemplate.opsForValue().get(USER_KEY+userId);
        return objectMapper.readValue(userJson,UserDto.class);
    }



}
