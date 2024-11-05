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

    @Authenticated
    @PostMapping("{novelId}/bookmark")
    public ResponseEntity<?> addBookmark(@PathVariable("novelId") final String novelId) {
        String memberId = AuthenticationAspect.getAuthenticatedMemberId();
        novelService.addBookmark(novelId, memberId);
        return SuccessResponse.toResponseEntity(NovelSuccessCode.BOOKMARKED_NOVEL);
    }

    @Authenticated
    @DeleteMapping("{novelId}/bookmark")
    public ResponseEntity<?> deleteBookmark(@PathVariable("novelId") final String novelId) {
        String memberId = AuthenticationAspect.getAuthenticatedMemberId();
        novelService.deleteBookmark(novelId, memberId);
        return SuccessResponse.toResponseEntity(NovelSuccessCode.UNBOOKMARKED_NOVEL);
    }
}
