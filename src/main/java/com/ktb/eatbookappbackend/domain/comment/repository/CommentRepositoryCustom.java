package com.ktb.eatbookappbackend.domain.comment.repository;

import com.ktb.eatbookappbackend.domain.episode.dto.CommentDTO;
import java.util.List;

public interface CommentRepositoryCustom {

    int countCommentsByEpisodeId(String episodeId);

    List<CommentDTO> findCommentDTOsByEpisodeId(String episodeId);
}
