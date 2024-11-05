package com.ktb.eatbookappbackend.domain.novel.controller;

import com.ktb.eatbookappbackend.domain.global.authentication.Authenticated;
import com.ktb.eatbookappbackend.domain.global.authentication.AuthenticationAspect;
import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.novel.message.NovelSuccessCode;
import com.ktb.eatbookappbackend.domain.novel.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
        System.out.println("여기 들어오긴 함.");
        String memberId = AuthenticationAspect.getAuthenticatedMemberId();
        novelService.addBookmark(novelId, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(SuccessResponseDTO.of(NovelSuccessCode.BOOKMARKED_NOVEL));
    }
}
