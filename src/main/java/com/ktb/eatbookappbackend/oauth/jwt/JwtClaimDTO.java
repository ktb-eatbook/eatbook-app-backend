package com.ktb.eatbookappbackend.oauth.jwt;

import com.ktb.eatbookappbackend.entity.constant.Role;

public record JwtClaimDTO(
    String memberId,
    Role role
) {

    public static JwtClaimDTO of(String memberId, Role role) {
        return new JwtClaimDTO(memberId, role);
    }
}
