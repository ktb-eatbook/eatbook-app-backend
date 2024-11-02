package com.ktb.eatbookappbackend.domain.readingLog.controller;

import com.ktb.eatbookappbackend.domain.global.authentication.Authenticated;
import com.ktb.eatbookappbackend.domain.global.authentication.AuthenticationAspect;
import com.ktb.eatbookappbackend.domain.global.message.GlobalSuccessMessage;
import com.ktb.eatbookappbackend.domain.global.reponse.SuccessResponseDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.ContinueReadingInfoDTO;
import com.ktb.eatbookappbackend.domain.readingLog.message.ReadingLogSuccessCode;
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

    /**
     * 인증된 멤버의 소설의 마지막으로 읽은 에피소드 정보를 가져옵니다.
     *
     * @param novelId 소설의 고유 식별자.
     * @return ResponseEntity로 마지막으로 읽은 에피소드 정보를 포함하거나, 결과가 없음을 나타내는 성공 메시지를 반환합니다. 응답 본문은 다음 구조를 갖는 SuccessResponseDTO입니다: - success: 성공
     * 여부를 나타내는 boolean. - message: 성공 메시지를 나타내는 string. - data: 다음 key-value 쌍을 포함하는 map: - continueReadingInfo: Optional로
     * ContinueReadingInfoDTO를 나타내는 마지막으로 읽은 에피소드 정보. 마지막으로 읽은 에피소드가 없는 경우, 이 필드는 null이 됩니다.
     */
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
        return ResponseEntity.ok(SuccessResponseDTO.of(ReadingLogSuccessCode.BOOKMARKS_RETRIEVED, data));
    }
}
