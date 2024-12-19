package com.ktb.eatbookappbackend.domain.block.repository;

import com.ktb.eatbookappbackend.entity.Block;
import com.ktb.eatbookappbackend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, String> {

    boolean existsByBlockerAndBlocked(Member blocker, Member blocked);
}
