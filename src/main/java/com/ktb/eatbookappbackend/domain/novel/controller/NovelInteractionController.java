package com.ktb.eatbookappbackend.domain.novel.controller;

import com.ktb.eatbookappbackend.domain.global.authentication.Authenticated;
import com.ktb.eatbookappbackend.domain.global.authentication.AuthenticationAspect;
import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.domain.novel.message.NovelSuccessCode;
import com.ktb.eatbookappbackend.domain.novel.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/novels")
public class NovelInteractionController {

    private final NovelService novelService;

    /**
     * 지정된 소설에 북마크를 추가합니다.
     *
     * @param novelId 소설의 고유 식별자
     * @return ResponseEntity로, 작업 성공을 나타내는 응답
     *         HTTP 상태 코드는 200(OK)이며, 본문에는 {@link NovelSuccessCode#BOOKMARKED_NOVEL} 코드를 가진 SuccessResponse가 포함됩니다.
     */
    @Authenticated
    @PostMapping("{novelId}/bookmark")
    public ResponseEntity<?> addBookmark(@PathVariable("novelId") final String novelId) {
        String memberId = AuthenticationAspect.getAuthenticatedMemberId();
        novelService.addBookmark(novelId, memberId);
        return SuccessResponse.toResponseEntity(NovelSuccessCode.BOOKMARKED_NOVEL);
    }

    /**
     * 지정된 소설에서 북마크를 제거합니다.
     *
     * @param novelId 소설의 고유 식별자
     * @return ResponseEntity로, 작업 성공을 나타내는 응답
     *         HTTP 상태 코드는 200(OK)이며, 본문에는 {@link NovelSuccessCode#UNBOOKMARKED_NOVEL} 코드를 가진 SuccessResponse가 포함됩니다.
     */
    @Authenticated
    @DeleteMapping("{novelId}/bookmark")
    public ResponseEntity<?> deleteBookmark(@PathVariable("novelId") final String novelId) {
        String memberId = AuthenticationAspect.getAuthenticatedMemberId();
        novelService.deleteBookmark(novelId, memberId);
        return SuccessResponse.toResponseEntity(NovelSuccessCode.UNBOOKMARKED_NOVEL);
    }
}
