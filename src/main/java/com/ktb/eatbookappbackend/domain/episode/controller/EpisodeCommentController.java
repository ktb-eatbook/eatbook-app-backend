package com.ktb.eatbookappbackend.domain.episode.controller;

import com.ktb.eatbookappbackend.domain.episode.dto.CommentDetailDTO;
import com.ktb.eatbookappbackend.domain.episode.dto.EpisodeCommentsDTO;
import com.ktb.eatbookappbackend.domain.episode.message.EpisodeSuccessCode;
import com.ktb.eatbookappbackend.domain.episode.service.EpisodeCommentService;
import com.ktb.eatbookappbackend.entity.constant.Role;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/episodes")
@RequiredArgsConstructor
public class EpisodeCommentController {

    private final EpisodeCommentService episodeCommentService;

    /**
     * 특정 에피소드에 대한 댓글을 가져옵니다.
     *
     * @param episodeId 에피소드의 고유 식별자.
     * @return ResponseEntity로, SuccessResponseDTO를 포함하는 응답. 이 응답에는 상태 코드와 에피소드에 대한 댓글 데이터를 포함하는 EpisodeCommentsDTO 객체가 있습니다.
     */
    @Secured(Role.MEMBER_VALUE)
    @GetMapping("/{episodeId}/comments")
    public ResponseEntity<SuccessResponseDTO> getComments(@PathVariable("episodeId") String episodeId) {
        EpisodeCommentsDTO episodeCommentsDTO = episodeCommentService.getCommentsByEpisodeId(episodeId);
        return SuccessResponse.toResponseEntity(EpisodeSuccessCode.COMMENTS_RETRIEVED, episodeCommentsDTO);
    }

    /**
     * 특정 에피소드에 대한 새로운 댓글을 생성합니다.
     *
     * @param episodeId 에피소드의 고유 식별자.
     * @param memberId  댓글을 생성하는 멤버의 고유 식별자.
     * @param request   댓글 내용을 포함하는 요청 객체.
     * @return ResponseEntity로, SuccessResponseDTO를 포함하는 응답. 이 응답에는 성공 상태 코드와 생성된 댓글 데이터를 CommentDTO 객체로 포함합니다.
     */
    @Secured(Role.MEMBER_VALUE)
    @PostMapping("/{episodeId}/comments")
    public ResponseEntity<SuccessResponseDTO> createComment(
        @PathVariable("episodeId") String episodeId,
        @AuthenticationPrincipal String memberId,
        @RequestBody EpisodeCommentRequestDTO request
    ) {
        CommentDetailDTO comment = episodeCommentService.createComment(episodeId, memberId, request);
        return SuccessResponse.toResponseEntity(EpisodeSuccessCode.COMMENT_CREATED, comment);
    }

    /**
     * 특정 에피소드에 대한 댓글을 삭제합니다.
     *
     * @param episodeId 에피소드의 고유 식별자.
     * @param commentId 삭제할 댓글의 고유 식별자.
     * @return ResponseEntity로, SuccessResponseDTO를 포함하는 응답. 이 응답에는 성공 여부를 나타내는 상태 코드가 포함됩니다.
     */
    @Secured(Role.MEMBER_VALUE)
    @DeleteMapping("/{episodeId}/comments/{commentId}")
    public ResponseEntity<SuccessResponseDTO> deleteComment(
        @PathVariable("episodeId") String episodeId,
        @PathVariable("commentId") String commentId,
        @AuthenticationPrincipal String memberId
    ) {
        episodeCommentService.deleteComment(commentId, memberId);
        return SuccessResponse.toResponseEntity(EpisodeSuccessCode.COMMENT_DELETED);
    }
}
