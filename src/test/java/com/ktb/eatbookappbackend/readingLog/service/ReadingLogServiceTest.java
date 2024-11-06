package com.ktb.eatbookappbackend.readingLog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.domain.novel.dto.ContinueReadingInfoDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.LastReadEpisodeDTO;
import com.ktb.eatbookappbackend.domain.readingLog.repository.ReadingLogRepository;
import com.ktb.eatbookappbackend.domain.readingLog.service.ReadingLogService;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.ReadingLog;
import com.ktb.eatbookappbackend.member.fixture.MemberFixture;
import com.ktb.eatbookappbackend.novel.fixture.NovelFixture;
import com.ktb.eatbookappbackend.readingLog.fixture.ReadingLogFixture;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReadingLogServiceTest {

    @Mock
    private ReadingLogRepository readingLogRepository;

    @InjectMocks
    private ReadingLogService readingLogService;

    @Test
    void should_ReturnContinueReadingInfoDTO_When_ReadingLogExists() {
        // given
        Member member = MemberFixture.createMember();
        Novel novel = NovelFixture.createNovel();
        ReadingLog readingLog = ReadingLogFixture.createReadingLog(member, novel);

        when(readingLogRepository.findLastReadEpisode(member.getId(), novel.getId())).thenReturn(Optional.of(readingLog));

        // when
        Optional<ContinueReadingInfoDTO> result = readingLogService.getLastReadEpisode(member.getId(), novel.getId());

        // then
        assertNotNull(result);
        LastReadEpisodeDTO lastReadEpisode = result.get().continueReadingInfo();
        assertEquals(readingLog.getEpisode().getId(), lastReadEpisode.episodeId());
        assertEquals(readingLog.getEpisode().getChapterNumber(), lastReadEpisode.chapterNumber());
        assertEquals(readingLog.getEpisode().getTitle(), lastReadEpisode.title());
        assertEquals(readingLog.getPageNumber(), lastReadEpisode.pageNumber());
        assertEquals(readingLog.getTtsLastPositionSeconds().toString(), lastReadEpisode.ttsLastPositionSeconds());
    }

    @Test
    void should_ReturnEmptyResult_When_ReadingLogDoesNotExist() {
        // given
        Member member = MemberFixture.createMember();
        Novel novel = NovelFixture.createNovel();
        ReadingLog readingLog = ReadingLogFixture.createReadingLog(member, novel);

        when(readingLogRepository.findLastReadEpisode(member.getId(), novel.getId())).thenReturn(Optional.empty());

        // when
        Optional<ContinueReadingInfoDTO> continueReadingInfo = readingLogService.getLastReadEpisode(member.getId(), novel.getId());

        // then
        assertTrue(continueReadingInfo.isEmpty());
    }
}
