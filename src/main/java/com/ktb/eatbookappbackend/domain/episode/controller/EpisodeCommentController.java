package com.ktb.eatbookappbackend.domain.episode.controller;

import com.ktb.eatbookappbackend.domain.episode.dto.CommentDTO;
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
@RequestMapping("/api/episodes/{episodeId}/comments")
@RequiredArgsConstructor
public class EpisodeCommentController {

    private final EpisodeCommentService episodeCommentService;

    @GetMapping
    public ResponseEntity<SuccessResponseDTO> getComments(@PathVariable("episodeId") String episodeId) {
        EpisodeCommentsDTO episodeCommentsDTO = episodeCommentService.getCommentsByEpisodeId(episodeId);
        return SuccessResponse.toResponseEntity(EpisodeSuccessCode.COMMENTS_RETRIEVED, episodeCommentsDTO);
    }

    @Secured(Role.MEMBER_VALUE)
    @PostMapping
    public ResponseEntity<SuccessResponseDTO> createComment(
        @PathVariable("episodeId") String episodeId,
        @AuthenticationPrincipal String memberId,
        @RequestBody EpisodeCommentRequestDTO request
    ) {
        CommentDTO comment = episodeCommentService.createComment(episodeId, memberId, request);
        return SuccessResponse.toResponseEntity(EpisodeSuccessCode.COMMENT_CREATED, comment);
    }

    @Secured(Role.MEMBER_VALUE)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponseDTO> deleteComment(
        @PathVariable("episodeId") String episodeId,
        @PathVariable("commentId") String commentId
    ) {
        episodeCommentService.deleteComment(commentId);
        return SuccessResponse.toResponseEntity(EpisodeSuccessCode.COMMENT_DELETED);
    }
}
