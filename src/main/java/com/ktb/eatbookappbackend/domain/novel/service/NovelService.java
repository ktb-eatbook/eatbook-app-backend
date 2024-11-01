package com.ktb.eatbookappbackend.domain.novel.service;

import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.entity.Novel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NovelService {
    private final NovelRepository novelRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 고유 식별자로 소설을 찾습니다.
     *
     * @param novelId 찾을 소설의 고유 식별자.
     * @return 찾은 소설. 지정된 식별자로 소설을 찾지 못하면
     *         {@link NovelException}이 발생하고 {@link NovelErrorCode#NOVEL_NOT_FOUND}가 전달됩니다.
     */
    public Novel findById(String novelId) {
        return novelRepository.findById(novelId)
               .orElseThrow(() -> new NovelException(NovelErrorCode.NOVEL_NOT_FOUND));
    }

    /**
     * 지정된 novelId로 소설과 즐겨찾기 수를 검색합니다.
     *
     * @param novelId 검색할 소설의 고유 식별자.
     * @return {@link NovelDTO} 객체로, 소설의 정보와 좋아요 수를 포함합니다.
     *         지정된 novelId로 소설을 찾을 수 없으면 {@link NovelException}이 발생하고
     *         {@link NovelErrorCode#NOVEL_NOT_FOUND}가 전달됩니다.
     */
    public NovelDTO getNovel(String novelId) {
        Novel novel = findById(novelId);
        int favoriteCount = favoriteRepository.countByNovelId(novelId);
        return NovelDTO.of(novel, favoriteCount);
    }
}
