package com.ktb.eatbookappbackend.domain.episode.service;

import com.ktb.eatbookappbackend.domain.comment.repository.CommentRepository;
import com.ktb.eatbookappbackend.domain.episode.dto.CommentDTO;
import com.ktb.eatbookappbackend.domain.episode.dto.EpisodeCommentsDTO;
import com.ktb.eatbookappbackend.entity.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class EpisodeCommentService {

    private final CommentRepository commentRepository;

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
}
