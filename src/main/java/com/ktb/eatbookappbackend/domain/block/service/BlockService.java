package com.ktb.eatbookappbackend.domain.block.service;

import com.ktb.eatbookappbackend.domain.block.dto.BlockedMemberIdsDTO;
import com.ktb.eatbookappbackend.domain.block.exception.BlockException;
import com.ktb.eatbookappbackend.domain.block.message.BlockErrorCode;
import com.ktb.eatbookappbackend.domain.block.repository.BlockRepository;
import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.entity.Block;
import com.ktb.eatbookappbackend.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void blockMember(String blockerId, String blockedId) {
        Member blocker = memberRepository.findById(blockerId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member blocked = memberRepository.findById(blockedId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (blockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            throw new BlockException(BlockErrorCode.ALREADY_BLOCKED);
        }

        Block block = Block.builder()
            .blocker(blocker)
            .blocked(blocked)
            .build();
        blockRepository.save(block);
    }

    @Transactional(readOnly = true)
    public BlockedMemberIdsDTO getBlockedUserIds(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<Block> blockedList = blockRepository.findByBlocker(member);
        List<String> blockedMemberIds = blockedList.stream().map(block -> block.getBlocked().getId()).toList();
        return BlockedMemberIdsDTO.of(blockedMemberIds);
    }
}
