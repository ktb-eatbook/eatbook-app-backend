package com.ktb.eatbookappbackend.domain.member.repository;

import com.ktb.eatbookappbackend.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findById(String id);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
