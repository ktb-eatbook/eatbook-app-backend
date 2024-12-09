package com.ktb.eatbookappbackend.domain.refreshToken.repository;

import com.ktb.eatbookappbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

}
