package com.ktb.eatbookappbackend.domain.readingLog.service;

import com.ktb.eatbookappbackend.domain.novel.dto.ContinueReadingInfoDTO;
import com.ktb.eatbookappbackend.domain.readingLog.repository.ReadingLogRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReadingLogService {

    private final ReadingLogRepository readingLogRepository;

    @Transactional(readOnly = true)
    public Optional<ContinueReadingInfoDTO> getLastReadEpisode(String memberId, String novelId) {
        return readingLogRepository.findLastReadEpisode(memberId, novelId)
            .map(readingLog -> ContinueReadingInfoDTO.of(
                readingLog.getEpisode().getId(),
                readingLog.getEpisode().getChapterNumber(),
                readingLog.getEpisode().getTitle(),
                readingLog.getPageNumber(),
                readingLog.getTtsLastPositionSeconds().toString()
            ));
    }
}
