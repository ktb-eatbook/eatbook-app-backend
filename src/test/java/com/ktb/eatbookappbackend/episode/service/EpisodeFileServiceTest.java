package com.ktb.eatbookappbackend.episode.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.domain.episode.message.EpisodeFileErrorCode;
import com.ktb.eatbookappbackend.domain.episode.service.EpisodeFileService;
import com.ktb.eatbookappbackend.domain.episode.service.FileService;
import com.ktb.eatbookappbackend.domain.fileMetaData.repository.FileMetaDataRepository;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelFileException;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.FileMetadata;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import com.ktb.eatbookappbackend.episode.fixture.EpisodeFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EpisodeFileServiceTest {

    @Mock
    private FileMetaDataRepository fileMetaDataRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private EpisodeFileService episodeFileService;

    private final Episode episode = EpisodeFixture.createEpisode();
    private final FileType fileType = FileType.TTS;
    private final String filePath = "https://example.com";
    private FileMetadata fileMetadata;

    @BeforeEach
    public void setUp() {
        fileMetadata = EpisodeFixture.createFileMetadata(FileType.TTS, "https://example.com", episode);
    }

    @Test
    public void should_GeneratePresignedGetUrl_When_FileExists() {
        // Given
        when(fileMetaDataRepository.findFileIdByEpisodeIdAndType(episode.getId(), fileType))
            .thenReturn(Optional.of(fileMetadata));
        when(fileService.generatePresignedGetUrl(fileMetadata)).thenReturn(filePath);

        // When
        String result = episodeFileService.generatePresignedGetUrl(episode.getId(), fileType);

        // Then
        assertEquals(filePath, result);
    }

    @Test
    public void should_ThrowException_When_FileNotFound() {
        // Given
        when(fileMetaDataRepository.findFileIdByEpisodeIdAndType(episode.getId(), fileType))
            .thenReturn(Optional.empty());

        // When
        NovelFileException exception = assertThrows(
            NovelFileException.class,
            () -> episodeFileService.generatePresignedGetUrl(episode.getId(), fileType)
        );

        // Then
        assertEquals(EpisodeFileErrorCode.FILE_NOT_FOUND, exception.getErrorCode());
    }
}
