package com.ktb.eatbookappbackend.domain.novel.controller;

import com.ktb.eatbookappbackend.domain.episode.dto.EpisodeDTO;
import com.ktb.eatbookappbackend.domain.global.reponse.FailureResponseDTO;
import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.message.NovelSuccessCode;
import com.ktb.eatbookappbackend.domain.novel.service.NovelService;
import com.ktb.eatbookappbackend.entity.constant.EpisodeSortOrder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @return 성공적으로 검색된 소설 세부 정보를 포함하는 ResponseEntity, 또는 소설을 찾을 수 없는 경우 FailureResponseDTO와 적절한 오류 코드를 반환합니다.
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

    /**
     * 지정된 소설에 대한 에피소드 목록을 지정된 정렬 순서로 검색합니다.
     *
     * @param novelId 검색할 소설의 고유 식별자.
     * @param sortOrder 에피소드 정렬 순서. 제공되지 않으면 기본값은 "latest"입니다.
     * @return 성공적으로 검색된 에피소드 목록이 포함된 ResponseEntity.
     *         소설을 찾을 수 없는 경우 FailureResponseDTO와 적절한 오류 코드가 포함된 ResponseEntity를 반환합니다.
     * @throws NovelException 에피소드 검색 중에 오류가 발생할 경우.
     */
    @GetMapping("/{novelId}/episodes")
    public ResponseEntity<?> getEpisodes(
        @PathVariable("novelId") final String novelId,
        @RequestParam(value = "sort", required = false, defaultValue = "latest") EpisodeSortOrder sortOrder
    ) {
        try {
            List<EpisodeDTO> episodes = novelService.getEpisodes(novelId, sortOrder);
            return ResponseEntity.ok(SuccessResponseDTO.of(NovelSuccessCode.EPISODES_RETRIEVED, episodes));
        } catch (NovelException e) {
            return ResponseEntity.badRequest()
                .body(FailureResponseDTO.of(NovelErrorCode.NOVEL_NOT_FOUND));
        }
    }
}
