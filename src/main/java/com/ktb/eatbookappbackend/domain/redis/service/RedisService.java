package com.ktb.eatbookappbackend.domain.redis.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public int incrementViewCount(String novelId) {
        String key = generateKey(novelId);
        return redisTemplate.opsForValue().increment(key).intValue();
    }

    public int getViewCount(String novelId) {
        String key = generateKey(novelId);
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    public Set<String> getAllViewKeys() {
        return redisTemplate.keys("novel:view:*");
    }

    public void resetViewCount(String key) {
        redisTemplate.delete(key);
    }

    private String generateKey(String novelId) {
        return "novel:view:" + novelId;
    }
}

