package com.ktb.eatbookappbackend.domain.redis.service;

import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ViewCountBatchService {

    private final RedisService redisService;
    private final NovelRepository novelRepository;

    /**
     * Redis에 저장된 조회수를 KST 기준으로 5분 간격으로 DB에 반영하고, Redis 데이터를 초기화합니다.
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void syncViewCountsToDB() {
        Set<String> keys = redisService.getAllViewKeys();
        if (keys.isEmpty()) {
            log.info("No view count keys found in Redis.");
            return;
        }

        for (String key : keys) {
            try {
                String novelId = key.replace("novel:view:", "");
                int redisViewCount = redisService.getViewCount(novelId);

                novelRepository.findById(novelId).ifPresent(novel -> {
                    int dbViewCount = novel.getViewCount();
                    int totalViewCount = dbViewCount + redisViewCount;
                    novel.setViewCount(totalViewCount);
                    novelRepository.save(novel);
                    log.info("Novel ID: %s, Redis: %d, DB: %d, Total: %d%n", novelId, redisViewCount, dbViewCount, totalViewCount);
                });

                redisService.resetViewCount(key);
            } catch (Exception e) {
                log.error("Error syncing view count for key: " + key);
                e.printStackTrace();
            }
        }
    }
}