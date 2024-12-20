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

    /**
     * 멤버를 차단합니다.
     *
     * @param blockerId 차단을 시작하는 멤버의 ID
     * @param blockedId 차단되는 멤버의 ID
     * @throws BlockException  차단자와 차단된 ID가 동일한 경우, 또는 차단이 이미 존재하는 경우에 발생
     * @throws MemberException 차단자 또는 차단된 멤버가 존재하지 않는 경우에 발생
     */
    @Transactional
    public void blockMember(String blockerId, String blockedId) {
        if (blockedId.equals(blockerId)) {
            throw new BlockException(BlockErrorCode.SELF_BLOCKED);
        }

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

    /**
     * 차단된 멤버 ID 목록을 검색합니다.
     *
     * @param memberId 차단된 멤버 ID 목록을 검색할 멤버의 ID
     * @return 차단된 멤버 ID 목록을 포함하는 {@link BlockedMemberIdsDTO} 객체
     * @throws MemberException 지정된 멤버 ID가 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public BlockedMemberIdsDTO getBlockedMemberIds(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<Block> blockedList = blockRepository.findByBlocker(member);
        List<String> blockedMemberIds = blockedList.stream().map(block -> block.getBlocked().getId()).toList();
        return BlockedMemberIdsDTO.of(blockedMemberIds);
    }
}
