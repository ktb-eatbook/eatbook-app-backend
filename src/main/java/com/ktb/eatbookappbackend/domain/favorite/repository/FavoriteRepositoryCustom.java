package com.ktb.eatbookappbackend.domain.favorite.repository;

import com.ktb.eatbookappbackend.domain.favorite.dto.NovelFavoriteCountDTO;
import java.util.List;

public interface FavoriteRepositoryCustom {

    public List<NovelFavoriteCountDTO> findFavoriteCountsByNovelIds(List<String> novelIds);
}
