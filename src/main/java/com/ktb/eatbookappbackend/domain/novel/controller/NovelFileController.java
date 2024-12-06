package com.ktb.eatbookappbackend.domain.novel.controller;

import com.ktb.eatbookappbackend.domain.novel.dto.NovelFileDTO;
import com.ktb.eatbookappbackend.domain.novel.message.NovelFileSuccessCode;
import com.ktb.eatbookappbackend.domain.novel.service.NovelFileService;
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
public class NovelFileController {

    private final NovelFileService novelFileService;

    @GetMapping("/{episodeId}/{fileType}/presigned-url")
    public ResponseEntity<SuccessResponseDTO> getPresignedUrl(
        @PathVariable("episodeId") String episodeId,
        @PathVariable("fileType") String fileType
    ) {
        FileType enumFileType = FileType.valueOf(fileType.toUpperCase());
        String presignedUrl = novelFileService.generatePresignedGetUrl(episodeId, enumFileType);
        return SuccessResponse.toResponseEntity(NovelFileSuccessCode.PRESIGNED_URL_RETRIEVED, NovelFileDTO.of(presignedUrl));
    }
}
