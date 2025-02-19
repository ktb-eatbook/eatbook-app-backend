package com.ktb.eatbookappbackend.readingLog.service;

import com.ktb.eatbookappbackend.domain.episode.exception.EpisodeException;
import com.ktb.eatbookappbackend.domain.episode.message.EpisodeErrorCode;
import com.ktb.eatbookappbackend.domain.episode.repository.EpisodeRepository;
import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.readingLog.dto.ReadingLogDTO;
import com.ktb.eatbookappbackend.domain.readingLog.repository.ReadingLogRepository;
import com.ktb.eatbookappbackend.domain.readingLog.service.ReadingLogService;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.ReadingLog;
import com.ktb.eatbookappbackend.episode.fixture.EpisodeFixture;
import com.ktb.eatbookappbackend.member.fixture.MemberFixture;
import com.ktb.eatbookappbackend.novel.fixture.NovelFixture;
import com.ktb.eatbookappbackend.readingLog.fixture.ReadingLogFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadingLogServiceTest {

    @Mock
    private ReadingLogRepository readingLogRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private EpisodeRepository episodeRepository;

    @InjectMocks
    private ReadingLogService readingLogService;

    private Member member;
    private Novel novel;
    private Episode episode;
    private ReadingLogDTO readingLogDTO;

    @BeforeEach
    void setUp() {
        member = MemberFixture.createMember();
        novel = NovelFixture.createNovel();
        episode = EpisodeFixture.createEpisode(novel);

        readingLogDTO = ReadingLogFixture.createReadingLogDTO(member, novel, episode);
    }

    @Test
    void should_CreateReadingLog_When_ValidInput() {
        // Given
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(episodeRepository.findById(episode.getId())).thenReturn(Optional.of(episode));
        when(readingLogRepository.save(any(ReadingLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        assertDoesNotThrow(() -> readingLogService.createReadingLog(member.getId(), readingLogDTO));

        // Then
        verify(readingLogRepository, times(1)).save(any(ReadingLog.class));
    }

    @Test
    void should_ThrowMemberException_When_MemberNotFound() {
        // Given
        when(memberRepository.findById("invalid-member")).thenReturn(Optional.empty());

        // When & Then
        MemberException exception = assertThrows(MemberException.class,
                () -> readingLogService.createReadingLog("invalid-member", readingLogDTO));

        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void should_ThrowNovelException_When_NovelNotFound() {
        // Given
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());
        doReturn(Optional.empty()).when(novelRepository).findById(novel.getId());

        // When & Then
        NovelException exception = assertThrows(NovelException.class,
                () -> readingLogService.createReadingLog(member.getId(), readingLogDTO));

        assertEquals(NovelErrorCode.NOVEL_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void should_ThrowEpisodeException_When_EpisodeNotFound() {
        // Given
        doReturn(Optional.of(member)).when(memberRepository).findById(member.getId());
        doReturn(Optional.of(novel)).when(novelRepository).findById(novel.getId());
        doReturn(Optional.empty()).when(episodeRepository).findById(episode.getId());

        // When & Then
        EpisodeException exception = assertThrows(EpisodeException.class,
                () -> readingLogService.createReadingLog(member.getId(), readingLogDTO));

        assertEquals(EpisodeErrorCode.EPISODE_NOT_FOUND, exception.getErrorCode());
    }
}
