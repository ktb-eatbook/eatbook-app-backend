package com.ktb.eatbookappbackend.domain.redis.service;

import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class ViewCountBatchService {

    private final RedisService redisService;
    private final NovelRepository novelRepository;

    /**
     * Redis에 저장된 조회수를 KST 기준으로 5분 간격으로 DB에 반영하고, 반영 후 Redis 데이터를 초기화합니다.
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void syncViewCountsToDB() {
        Set<String> keys = redisService.getAllViewKeys();
        if (keys.isEmpty()) {
            log.info("No view count keys found in Redis.");
            return;
        }

        for (String key : keys) {
            String lockKey = "lock:" + key;
            boolean lockAcquired = redisService.acquireLock(lockKey, 5000);

            if (!lockAcquired) {
                log.info("Skipping key {} as lock is not acquired.", key);
                continue;
            }

            try {
                updateViewCount(key);
            } catch (Exception e) {
                log.error("Error syncing view count for key: " + key, e);
            } finally {
                redisService.releaseLock(lockKey);
            }
        }
    }

    @Transactional
    public void updateViewCount(String key) {
        String novelId = key.replace("novel:view:", "");
        int redisViewCount = redisService.getViewCount(novelId);

        if (redisViewCount == 0) {
            log.info("Skipping novel ID {} as Redis view count is 0.", novelId);
            return;
        }

        novelRepository.findById(novelId).ifPresent(novel -> {
            int dbViewCount = novel.getViewCount();
            int totalViewCount = dbViewCount + redisViewCount;
            novel.setViewCount(totalViewCount);
            novelRepository.save(novel);

            log.info("Novel ID: {}, Redis: {}, DB: {}, Total: {}",
                novelId, redisViewCount, dbViewCount, totalViewCount);
        });

        redisService.resetViewCount(key);
    }
}
