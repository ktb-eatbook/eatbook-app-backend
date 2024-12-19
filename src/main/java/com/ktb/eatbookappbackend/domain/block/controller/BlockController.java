package com.ktb.eatbookappbackend.domain.block.controller;

import com.ktb.eatbookappbackend.domain.block.message.BlockSuccessCode;
import com.ktb.eatbookappbackend.domain.block.service.BlockService;
import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/block")
@RequiredArgsConstructor
public class BlockController {

    private final BlockService blockService;

    @Secured(Role.MEMBER_VALUE)
    @PostMapping()
    public ResponseEntity<SuccessResponseDTO> blockUser(
        @AuthenticationPrincipal String memberId,
        @RequestParam(name = "id") String id
    ) {
        blockService.blockMember(memberId, id);
        return SuccessResponse.toResponseEntity(BlockSuccessCode.BLOCK_SUCCESS, null);
    }
}