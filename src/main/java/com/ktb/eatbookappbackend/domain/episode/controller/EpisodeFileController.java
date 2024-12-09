package com.ktb.eatbookappbackend.domain.episode.controller;

import com.ktb.eatbookappbackend.domain.episode.service.EpisodeFileService;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelFileDTO;
import com.ktb.eatbookappbackend.domain.episode.message.EpisodeFileSuccessCode;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/episodes")
public class EpisodeFileController {

    private final EpisodeFileService episodeFileService;

    @GetMapping("/{episodeId}/{fileType}/presigned-url")
    public ResponseEntity<SuccessResponseDTO> getPresignedUrl(
        @PathVariable("episodeId") String episodeId,
        @PathVariable("fileType") String fileType
    ) {
        FileType enumFileType = FileType.valueOf(fileType.toUpperCase());
        String presignedUrl = episodeFileService.generatePresignedGetUrl(episodeId, enumFileType);
        return SuccessResponse.toResponseEntity(EpisodeFileSuccessCode.PRESIGNED_URL_RETRIEVED, NovelFileDTO.of(presignedUrl));
    }
}
