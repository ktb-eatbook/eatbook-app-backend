package com.ktb.eatbookappbackend.domain.comment.repository;

import com.ktb.eatbookappbackend.domain.episode.dto.CommentDetailDTO;
import java.util.List;

public interface CommentRepositoryCustom {

    int countCommentsByEpisodeId(String episodeId);

    List<CommentDetailDTO> findCommentDetailDTOsByEpisodeId(String episodeId);
}
