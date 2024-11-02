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

    /**
     * 특정 멤버와 소설에 대한 마지막으로 읽은 에피소드 정보를 가져옵니다.
     *
     * @param memberId 멤버의 고유 식별자.
     * @param novelId 소설의 고유 식별자.
     * @return 마지막으로 읽은 에피소드에 대한 {@link ContinueReadingInfoDTO}를 포함하는 {@link Optional}.
     *         마지막으로 읽은 에피소드가 없는 경우, {@link Optional}은 비어 있습니다.
     */
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
