package com.ktb.eatbookappbackend.domain.readingLog.controller;

import com.ktb.eatbookappbackend.domain.global.authentication.Authenticated;
import com.ktb.eatbookappbackend.domain.global.authentication.AuthenticationAspect;
import com.ktb.eatbookappbackend.domain.global.message.GlobalSuccessMessage;
import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.ContinueReadingInfoDTO;
import com.ktb.eatbookappbackend.domain.readingLog.service.ReadingLogService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/reading-logs")
public class ReadingLogController {

    private final ReadingLogService readingLogService;

    @Authenticated
    @GetMapping("/novels/{novelId}/last-read")
    public ResponseEntity<?> getLastReadEpisode(@PathVariable("novelId") String novelId) {
        String memberId = AuthenticationAspect.getAuthenticatedMemberId();

        Optional<ContinueReadingInfoDTO> lastReadEpisodeOpt = readingLogService.getLastReadEpisode(memberId, novelId);

        if (lastReadEpisodeOpt.isEmpty()) {
            return ResponseEntity.ok(SuccessResponseDTO.of(GlobalSuccessMessage.NO_RESULTS_FOUND, null));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("continueReadingInfo", lastReadEpisodeOpt.get());
        return ResponseEntity.ok(SuccessResponseDTO.of(GlobalSuccessMessage.NO_RESULTS_FOUND, data));
    }
}
