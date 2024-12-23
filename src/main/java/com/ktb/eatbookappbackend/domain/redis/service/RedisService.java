package com.ktb.eatbookappbackend.domain.redis.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean acquireLock(String key, long timeout) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "LOCK", timeout, TimeUnit.MILLISECONDS);
        return result != null && result;
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }

    public int incrementViewCount(String novelId) {
        String key = buildViewKey(novelId);
        return redisTemplate.opsForValue().increment(key).intValue();
    }

    public int getViewCount(String novelId) {
        String key = buildViewKey(novelId);
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    public Set<String> getAllViewKeys() {
        return redisTemplate.keys("novel:view:*");
    }

    public void resetViewCount(String key) {
        redisTemplate.delete(key);
    }

    private String buildViewKey(String novelId) {
        return "novel:view:" + novelId;
    }
}
