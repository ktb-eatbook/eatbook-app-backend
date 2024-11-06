package com.ktb.eatbookappbackend.domain.member.controller;

import com.ktb.eatbookappbackend.domain.global.authentication.Authenticated;
import com.ktb.eatbookappbackend.domain.global.authentication.AuthenticationAspect;
import com.ktb.eatbookappbackend.domain.global.message.GlobalSuccessMessage;
import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.member.dto.BookmarkedNovelsPaginationDTO;
import com.ktb.eatbookappbackend.domain.member.message.MemberSuccessCode;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 특정 회원이 북마크한 소설 목록을 페이지네이션하여 조회합니다. 이 메소드는 보안되어 있으며 인증이 필요합니다.
     *
     * @param page 조회할 결과의 페이지 번호. 양의 정수여야 합니다.
     * @param size 페이지당 항목 수. 양의 정수여야 합니다.
     * @return 다음 중 하나를 포함하는 ResponseEntity: - 북마크된 소설 목록이 있는 경우, 페이지네이션된 목록을 포함한 SuccessResponseDTO - 북마크가 없는 경우, NO_RESULTS_FOUND 메시지를 포함한
     * SuccessResponseDTO - page 또는 size 매개변수가 유효하지 않은 경우, FailureResponseDTO
     * @throws RuntimeException 인증 실패 시
     */
    @Authenticated
    @GetMapping("/bookmarks")
    public ResponseEntity<?> getMemberBookmarkedNovels(@RequestParam(name = "page") @Min(1) final int page,
        @RequestParam(name = "size") @Min(1) final int size) {
        String memberId = AuthenticationAspect.getAuthenticatedMemberId();
        BookmarkedNovelsPaginationDTO bookmarkedNovels = memberService.getMemberBookmarkedNovels(memberId, page, size);
        if (bookmarkedNovels.bookmarkedNovels().isEmpty()) {
            return ResponseEntity.ok(SuccessResponseDTO.of(GlobalSuccessMessage.NO_RESULTS_FOUND, bookmarkedNovels));
        }

        return ResponseEntity.ok(SuccessResponseDTO.of(MemberSuccessCode.BOOKMARKS_RETRIEVED, bookmarkedNovels));
    }
}
