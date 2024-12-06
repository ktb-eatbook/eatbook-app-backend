package com.ktb.eatbookappbackend.domain.novel.service;

import com.ktb.eatbookappbackend.domain.fileMetaData.repository.FileMetaDataRepository;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelFileException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelFileErrorCode;
import com.ktb.eatbookappbackend.entity.FileMetadata;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NovelFileService {

    private final FileService fileService;
    private final FileMetaDataRepository fileMetaDataRepository;

    /**
     * 지정된 에피소드와 파일 유형에 해당하는 소설 파일을 검색하여
     * pre-signed URL을 생성합니다.
     *
     * @param episodeId 에피소드 ID
     * @param fileType 파일 유형 (예: EPUB, PDF)
     * @return 생성된 pre-signed URL
     * @throws NovelFileException 지정된 에피소드 ID와 파일 유형에 해당하는
     *                           소설 파일이 없는 경우
     */
    public String generatePresignedGetUrl(String episodeId, FileType fileType) {
        FileMetadata fileMetadata = fileMetaDataRepository.findFileIdByEpisodeIdAndType(episodeId, fileType)
            .orElseThrow(() -> new NovelFileException(NovelFileErrorCode.FILE_NOT_FOUND));
        return fileService.generatePresignedGetUrl(fileMetadata);
    }
}
