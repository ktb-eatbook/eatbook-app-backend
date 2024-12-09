package com.ktb.eatbookappbackend.domain.episode.controller;

import com.ktb.eatbookappbackend.domain.episode.dto.EpisodeCommentsDTO;
import com.ktb.eatbookappbackend.domain.episode.message.EpisodeCommentSuccessCode;
import com.ktb.eatbookappbackend.domain.episode.service.EpisodeCommentService;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponse;
import com.ktb.eatbookappbackend.global.reponse.SuccessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return SuccessResponse.toResponseEntity(EpisodeCommentSuccessCode.COMMENTS_RETRIEVED, episodeCommentsDTO);
    }
}
