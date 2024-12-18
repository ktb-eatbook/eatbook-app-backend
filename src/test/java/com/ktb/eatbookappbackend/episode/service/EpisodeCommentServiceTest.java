package com.ktb.eatbookappbackend.domain.episode.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.comment.fixture.CommentFixture;
import com.ktb.eatbookappbackend.domain.comment.repository.CommentRepository;
import com.ktb.eatbookappbackend.domain.episode.controller.EpisodeCommentRequestDTO;
import com.ktb.eatbookappbackend.domain.episode.dto.CommentDTO;
import com.ktb.eatbookappbackend.domain.episode.dto.EpisodeCommentsDTO;
import com.ktb.eatbookappbackend.domain.episode.exception.EpisodeException;
import com.ktb.eatbookappbackend.domain.episode.message.EpisodeErrorCode;
import com.ktb.eatbookappbackend.domain.episode.repository.EpisodeRepository;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import com.ktb.eatbookappbackend.entity.Comment;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.episode.fixture.EpisodeFixture;
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
public class EpisodeCommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private EpisodeRepository episodeRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private EpisodeCommentService episodeCommentService;

    private Episode episode;
    private Member member;

    @BeforeEach
    public void setUp() {
        episode = EpisodeFixture.createEpisode();
        member = MemberFixture.createMember();
    }

    @Test
    public void should_ReturnComments_When_EpisodeHasComments() {
        // Given
        Comment comment = CommentFixture.createComment(episode, member);
        when(commentRepository.findCommentsByEpisodeId(episode.getId())).thenReturn(List.of(comment));

        // When
        EpisodeCommentsDTO result = episodeCommentService.getCommentsByEpisodeId(episode.getId());

        // Then
        assertEquals(1, result.comments().size());
        assertEquals(comment.getContent(), result.comments().get(0).content());
        verify(commentRepository, times(1)).findCommentsByEpisodeId(episode.getId());
    }

    @Test
    public void should_CreateComment_When_EpisodeExists() {
        // Given
        EpisodeCommentRequestDTO request = new EpisodeCommentRequestDTO("New comment");
        Comment comment = CommentFixture.createComment(episode, member);
        when(episodeRepository.findById(any(String.class))).thenReturn(Optional.of(episode));
        when(memberService.findById(any(String.class))).thenReturn(member);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // When
        CommentDTO result = episodeCommentService.createComment(episode.getId(), member.getId(), request);

        // Then
        assertEquals(request.content(), result.content());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void should_ThrowException_When_EpisodeNotFoundOnCreate() {
        // Given
        when(episodeRepository.findById(any(String.class))).thenReturn(Optional.empty());
        EpisodeCommentRequestDTO request = new EpisodeCommentRequestDTO("New comment");

        // When & Then
        EpisodeException exception = assertThrows(EpisodeException.class,
            () -> episodeCommentService.createComment("invalid-episode-id", member.getId(), request));

        assertEquals(EpisodeErrorCode.EPISODE_NOT_FOUND, exception.getErrorCode());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    public void should_DeleteComment_When_CommentExists() {
        // Given
        Comment comment = CommentFixture.createComment(episode, member);
        when(commentRepository.findByIdAndDeletedAtIsNull(comment.getId())).thenReturn(Optional.of(comment));

        // When
        assertDoesNotThrow(() -> episodeCommentService.deleteComment(comment.getId()));

        // Then
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    public void should_ThrowException_When_CommentNotFoundOnDelete() {
        // Given
        when(commentRepository.findByIdAndDeletedAtIsNull(any(String.class))).thenReturn(Optional.empty());

        // When & Then
        EpisodeException exception = assertThrows(EpisodeException.class,
            () -> episodeCommentService.deleteComment("invalid-comment-id"));

        assertEquals(EpisodeErrorCode.COMMENT_NOT_FOUND, exception.getErrorCode());
        verify(commentRepository, never()).delete(any(Comment.class));
    }
}
