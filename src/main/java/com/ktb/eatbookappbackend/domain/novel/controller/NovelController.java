package com.ktb.eatbookappbackend.domain.novel.controller;

import com.ktb.eatbookappbackend.domain.global.reponse.FailureResponseDTO;
import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.message.NovelSuccessCode;
import com.ktb.eatbookappbackend.domain.novel.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/novel")
public class NovelController {
    private final NovelService novelService;

    @GetMapping("{novelId}")
    public ResponseEntity<?> getNovelDetails(@PathVariable("novelId") final String novelId) {
        try {
            NovelDTO novelInfo = novelService.getNovel(novelId);
            return ResponseEntity.ok(SuccessResponseDTO.of(NovelSuccessCode.NOVEL_RETRIEVED, novelInfo));
        } catch (NovelException e) {
            return ResponseEntity.badRequest()
                    .body(FailureResponseDTO.of(NovelErrorCode.NOVEL_NOT_FOUND));
        }
    }
}
