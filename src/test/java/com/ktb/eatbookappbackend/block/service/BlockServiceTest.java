package com.ktb.eatbookappbackend.block.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.domain.block.dto.BlockedMemberIdsDTO;
import com.ktb.eatbookappbackend.domain.block.exception.BlockException;
import com.ktb.eatbookappbackend.domain.block.message.BlockErrorCode;
import com.ktb.eatbookappbackend.domain.block.repository.BlockRepository;
import com.ktb.eatbookappbackend.domain.block.service.BlockService;
import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.entity.Block;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.member.fixture.MemberFixture;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BlockService blockService;

    private Member blocker;
    private Member blocked;

    @BeforeEach
    public void setUp() {
        blocker = MemberFixture.createMember();
        blocked = MemberFixture.createMember();
    }

    @Test
    public void should_BlockMember_When_ValidMembers() {
        // Given
        when(memberRepository.findById(blocker.getId())).thenReturn(Optional.of(blocker));
        when(memberRepository.findById(blocked.getId())).thenReturn(Optional.of(blocked));
        when(blockRepository.existsByBlockerAndBlocked(blocker, blocked)).thenReturn(false);

        // When
        assertDoesNotThrow(() -> blockService.blockMember(blocker.getId(), blocked.getId()));

        // Then
        verify(blockRepository, times(1)).save(any(Block.class));
    }

    @Test
    public void should_ThrowException_When_SelfBlocked() {
        // When & Then
        BlockException exception = assertThrows(BlockException.class,
            () -> blockService.blockMember(blocker.getId(), blocker.getId()));

        assertEquals(BlockErrorCode.SELF_BLOCKED, exception.getErrorCode());
        verify(blockRepository, never()).save(any(Block.class));
    }

    @Test
    public void should_ThrowException_When_MemberNotFound() {
        // Given
        when(memberRepository.findById(any(String.class))).thenReturn(Optional.empty());

        // When & Then
        MemberException exception = assertThrows(MemberException.class,
            () -> blockService.blockMember("invalid-blocker-id", blocked.getId()));

        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
        verify(blockRepository, never()).save(any(Block.class));
    }

    @Test
    public void should_ThrowException_When_AlreadyBlocked() {
        // Given
        when(memberRepository.findById(blocker.getId())).thenReturn(Optional.of(blocker));
        when(memberRepository.findById(blocked.getId())).thenReturn(Optional.of(blocked));
        when(blockRepository.existsByBlockerAndBlocked(blocker, blocked)).thenReturn(true);

        // When & Then
        BlockException exception = assertThrows(BlockException.class,
            () -> blockService.blockMember(blocker.getId(), blocked.getId()));

        assertEquals(BlockErrorCode.ALREADY_BLOCKED, exception.getErrorCode());
        verify(blockRepository, never()).save(any(Block.class));
    }

    @Test
    public void should_ReturnBlockedMemberIds_When_MembersBlocked() {
        // Given
        Block block = Block.builder().blocker(blocker).blocked(blocked).build();
        when(memberRepository.findById(blocker.getId())).thenReturn(Optional.of(blocker));
        when(blockRepository.findByBlocker(blocker)).thenReturn(List.of(block));

        // When
        BlockedMemberIdsDTO result = blockService.getBlockedMemberIds(blocker.getId());

        // Then
        assertEquals(1, result.blockedMemberIds().size());
        assertEquals(blocked.getId(), result.blockedMemberIds().get(0));
    }

    @Test
    public void should_ThrowException_When_BlockerNotFound() {
        // Given
        when(memberRepository.findById(any(String.class))).thenReturn(Optional.empty());

        // When & Then
        MemberException exception = assertThrows(MemberException.class,
            () -> blockService.getBlockedMemberIds("invalid-blocker-id"));

        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
        verify(blockRepository, never()).findByBlocker(any(Member.class));
    }
}
