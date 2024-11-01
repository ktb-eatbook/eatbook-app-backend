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

    
    /**
     * 지정된 novelId를 기반으로 소설의 세부 정보를 검색합니다.
     *
     * @param novelId 검색할 소설의 고유 식별자.
     * @return 성공적으로 검색된 소설 세부 정보를 포함하는 ResponseEntity,
     *         또는 소설을 찾을 수 없는 경우 FailureResponseDTO와 적절한 오류 코드를 반환합니다.
     * @throws NovelException 소설 검색 중에 오류가 발생할 경우.
     */
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
