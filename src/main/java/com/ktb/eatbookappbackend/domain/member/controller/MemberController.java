package com.ktb.eatbookappbackend.domain.member.controller;

import com.ktb.eatbookappbackend.domain.global.authentication.Authenticated;
import com.ktb.eatbookappbackend.domain.global.authentication.AuthenticationAspect;
import com.ktb.eatbookappbackend.domain.global.dto.PaginationWithDataDTO;
import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import com.ktb.eatbookappbackend.domain.global.reponse.FailureResponseDTO;
import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.member.dto.MemberBookmarkedNovelDTO;
import com.ktb.eatbookappbackend.domain.member.message.MemberSuccessCode;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Authenticated
    @GetMapping("/bookmarks")
    public ResponseEntity<?> getMemberBookmarkedNovels(@RequestParam final int page, @RequestParam final int size) {
        String memberId = AuthenticationAspect.getAuthenticatedMemberId();
        if (page < 1 || size < 1) {
            return ResponseEntity.badRequest()
                    .body(FailureResponseDTO.of(MessageCode.GlobalErrorMessage.INVALID_PARAMETER));
        }

        PaginationWithDataDTO<MemberBookmarkedNovelDTO> bookmarkedNovels = memberService.getMemberBookmarkedNovels(memberId, page, size);

        if (page - 1 > bookmarkedNovels.pagination().totalPages()) {
            return ResponseEntity.badRequest()
                    .body(FailureResponseDTO.of(MessageCode.GlobalErrorMessage.INVALID_PARAMETER));
        }

        if (bookmarkedNovels.data().get("novels").isEmpty()) {
            return ResponseEntity.ok(SuccessResponseDTO.of(MemberSuccessCode.NO_RESULTS_FOUND, bookmarkedNovels));
        }

        return ResponseEntity.ok(SuccessResponseDTO.of(MemberSuccessCode.BOOKMARKS_RETRIEVED, bookmarkedNovels));
    }
}
