package com.ktb.eatbookappbackend.domain.block.controller;

import com.ktb.eatbookappbackend.domain.block.dto.BlockedMemberIdsDTO;
import com.ktb.eatbookappbackend.domain.block.message.BlockSuccessCode;
import com.ktb.eatbookappbackend.domain.block.service.BlockService;
import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    /**
     * 현재 멤버를 대상으로 다른 멤버를 차단합니다.
     *
     * @param memberId 차단할 멤버를 식별하는 현재 멤버의 ID. 이 ID는 인증 주체로부터 얻습니다.
     * @param id       차단할 멤버를 식별하는 ID. 이 ID는 요청 매개변수로부터 얻습니다.
     * @return 차단 작업 결과를 포함하는 ResponseEntity. SuccessResponseDTO에는 차단 작업 결과를 나타내는 성공 코드 (BlockSuccessCode.BLOCK_SUCCESS)와 null 페이로드가 포함됩니다.
     * @throws IllegalArgumentException memberId 또는 id가 null이거나 비어 있는 경우.
     */
    @Secured(Role.MEMBER_VALUE)
    @PostMapping()
    public ResponseEntity<SuccessResponseDTO> blockMember(
        @AuthenticationPrincipal String memberId,
        @RequestParam(name = "id") String id
    ) {
        blockService.blockMember(memberId, id);
        return SuccessResponse.toResponseEntity(BlockSuccessCode.BLOCK_SUCCESS, null);
    }

    /**
     * 차단된 멤버 ID 목록을 가져옵니다.
     *
     * @param memberId 차단된 멤버 ID 목록을 가져올 멤버를 식별하는 ID. 이 ID는 인증 주체로부터 얻습니다.
     * @return 차단된 멤버 ID 목록을 포함하는 ResponseEntity. SuccessResponseDTO에는 차단된 멤버 ID 목록을 나타내는 성공 코드
     * (BlockSuccessCode.BLOCKED_MEMBER_IDS_RETRIEVED)와 차단된 멤버 ID 목록이 포함됩니다.
     */
    @Secured(Role.MEMBER_VALUE)
    @GetMapping("/blocked-member-ids")
    public ResponseEntity<SuccessResponseDTO> getBlockedMembers(@AuthenticationPrincipal String memberId) {
        BlockedMemberIdsDTO blockedMemberIds = blockService.getBlockedMemberIds(memberId);
        return SuccessResponse.toResponseEntity(BlockSuccessCode.BLOCKED_MEMBER_IDS_RETRIEVED, blockedMemberIds);
    }
}