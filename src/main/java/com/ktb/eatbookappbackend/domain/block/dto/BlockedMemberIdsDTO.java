package com.ktb.eatbookappbackend.domain.block.dto;

import java.util.List;

public record BlockedMemberIdsDTO(
    List<String> blockedMemberIds
) {

    public static BlockedMemberIdsDTO of(List<String> blockedMemberIds) {
        return new BlockedMemberIdsDTO(blockedMemberIds);
    }
}
