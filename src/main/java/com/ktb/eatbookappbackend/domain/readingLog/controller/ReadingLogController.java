package com.ktb.eatbookappbackend.domain.readingLog.controller;

import com.ktb.eatbookappbackend.domain.readingLog.dto.ReadingLogDTO;
import com.ktb.eatbookappbackend.domain.readingLog.message.ReadingLogSuccessCode;
import com.ktb.eatbookappbackend.domain.readingLog.service.ReadingLogService;
import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/reading-logs")
public class ReadingLogController {

    private final ReadingLogService readingLogService;

    /**
     * 새로운 ReadingLog를 생성합니다.
     *
     * @param memberId  현재 로그인한 사용자의 ID
     * @param requestDTO 요청 데이터 (pageNumber, ttsLastPositionSeconds, novelId, episodeId)
     * @return 생성된 ReadingLog 정보
     */
    @Secured(Role.MEMBER_VALUE)
    @PostMapping()
    public ResponseEntity<SuccessResponseDTO> createReadingLog(
            @AuthenticationPrincipal String memberId,
            @RequestBody ReadingLogDTO requestDTO
    ) {
        readingLogService.createReadingLog(memberId, requestDTO);
        return SuccessResponse.toResponseEntity(ReadingLogSuccessCode.SUCCESS_CREATED, null);
    }
}
