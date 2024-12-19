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

    /**
     * 특정 에피소드에 대한 모든 댓글을 검색합니다.
     *
     * @param episodeId 에피소드의 고유 식별자.
     * @return EpisodeCommentsDTO, 지정된 에피소드에 대한 댓글 목록을 포함합니다.
     * @throws EpisodeException 지정된 에피소드가 존재하지 않는 경우.
     */
    @Transactional(readOnly = true)
    public EpisodeCommentsDTO getCommentsByEpisodeId(String episodeId) {
        List<CommentDTO> commentDTOs = commentRepository.findCommentDTOsByEpisodeId(episodeId);
        return EpisodeCommentsDTO.of(commentDTOs);
    }

    /**
     * 특정 에피소드에 대한 새로운 댓글을 생성합니다.
     *
     * @param episodeId 에피소드의 고유 식별자.
     * @param memberId  댓글을 작성하는 멤버의 고유 식별자.
     * @param request   댓글 내용을 포함하는 요청 객체.
     * @return CommentDTO, 새로 생성된 댓글을 나타내는 DTO.
     * @throws EpisodeException 지정된 에피소드가 존재하지 않는 경우.
     */
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
            member.getId(),
            member.getNickname(),
            member.getProfileImageUrl(),
            comment.getCreatedAt()
        );
    }

    /**
     * 데이터베이스에서 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 고유 식별자.
     * @throws EpisodeException 지정된 댓글이 존재하지 않는 경우.
     */
    @Transactional
    public void deleteComment(String commentId, String memberId) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
            .orElseThrow(() -> new EpisodeException(EpisodeErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new EpisodeException(EpisodeErrorCode.COMMENT_DELETE_PERMISSION_DENIED);
        }

        comment.delete();
    }

    /**
     * 댓글의 신고 횟수를 증가시킵니다.
     *
     * @param commentId
     */
    @Transactional
    public void reportComment(String commentId) {
        commentRepository.incrementReportCount(commentId);

        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
            .orElseThrow(() -> new EpisodeException(EpisodeErrorCode.COMMENT_NOT_FOUND));

        if (comment.getReportCount() >= 5) {
            comment.delete();
        }
    }
}
