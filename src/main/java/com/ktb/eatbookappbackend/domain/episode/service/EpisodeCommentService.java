package com.ktb.eatbookappbackend.domain.episode.service;

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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EpisodeCommentService {

    private final CommentRepository commentRepository;
    private final EpisodeRepository episodeRepository;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public EpisodeCommentsDTO getCommentsByEpisodeId(String episodeId) {
        List<Comment> comments = commentRepository.findCommentsByEpisodeId(episodeId);

        List<CommentDTO> commentDTOs = comments.stream()
            .map(comment -> CommentDTO.of(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt()
            ))
            .toList();

        return EpisodeCommentsDTO.of(commentDTOs);
    }

    @Transactional
    public CommentDTO createComment(String episodeId, String memberId, EpisodeCommentRequestDTO request) {
        Episode episode = episodeRepository.findById(episodeId).orElseThrow(() -> new EpisodeException(EpisodeErrorCode.EPISODE_NOT_FOUND));
        Member member = memberService.findById(memberId);
        Comment comment = Comment.builder()
            .content(request.content())
            .episode(episode)
            .member(member)
            .build();
        commentRepository.save(comment);

        return CommentDTO.of(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt()
        );
    }

    @Transactional
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
            .orElseThrow(() -> new EpisodeException(EpisodeErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }
}
