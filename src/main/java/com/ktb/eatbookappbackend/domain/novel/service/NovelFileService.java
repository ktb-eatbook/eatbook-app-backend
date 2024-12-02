package com.ktb.eatbookappbackend.domain.novel.service;

import com.ktb.eatbookappbackend.domain.fileMetaData.repository.FileMetaDataRepository;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelFileException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelFileErrorCode;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NovelFileService {

    private final FileService fileService;
    private final FileMetaDataRepository fileMetaDataRepository;

    public String generatePresignedGetUrl(String novelId, String episodeId, FileType fileType) {
        String fileId = fileMetaDataRepository.findFileIdByEpisodeIdAndType(episodeId, fileType)
            .orElseThrow(() -> new NovelFileException(NovelFileErrorCode.FILE_NOT_FOUND));
        return fileService.generatePresignedGetUrl(novelId, episodeId, fileType, fileId);
    }
}
