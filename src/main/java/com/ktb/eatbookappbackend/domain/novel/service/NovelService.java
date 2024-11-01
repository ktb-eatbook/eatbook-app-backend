package com.ktb.eatbookappbackend.domain.novel.service;

import com.ktb.eatbookappbackend.domain.comment.repository.CommentRepository;
import com.ktb.eatbookappbackend.domain.episode.dto.EpisodeDTO;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.fileMetaData.repository.FileMetaDataRepository;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.constant.EpisodeSortOrder;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NovelService {

    private final NovelRepository novelRepository;
    private final FavoriteRepository favoriteRepository;
    private final FileMetaDataRepository fileMetaDataRepository;
    private final CommentRepository commentRepository;

    /**
     * 고유 식별자로 소설을 찾습니다.
     *
     * @param novelId 찾을 소설의 고유 식별자.
     * @return 찾은 소설. 지정된 식별자로 소설을 찾지 못하면 {@link NovelException}이 발생하고 {@link NovelErrorCode#NOVEL_NOT_FOUND}가 전달됩니다.
     */
    public Novel findById(String novelId) {
        return novelRepository.findById(novelId)
            .orElseThrow(() -> new NovelException(NovelErrorCode.NOVEL_NOT_FOUND));
    }

    /**
     * 지정된 novelId로 소설과 즐겨찾기 수를 검색합니다.
     *
     * @param novelId 검색할 소설의 고유 식별자.
     * @return {@link NovelDTO} 객체로, 소설의 정보와 좋아요 수를 포함합니다. 지정된 novelId로 소설을 찾을 수 없으면 {@link NovelException}이 발생하고
     * {@link NovelErrorCode#NOVEL_NOT_FOUND}가 전달됩니다.
     */
    public NovelDTO getNovel(String novelId) {
        Novel novel = findById(novelId);
        int favoriteCount = favoriteRepository.countByNovelId(novelId);
        return NovelDTO.of(novel, favoriteCount);
    }

    /**
     * 지정된 소설 ID에 해당하는 에피소드 목록을 정렬 순서에 따라 검색합니다.
     *
     * @param novelId   에피소드를 검색할 소설의 고유 식별자.
     * @param sortOrder 에피소드 정렬 순서. EpisodeSortOrder.oldest로 지정하면 에피소드가 출시일 순으로 오름차순 정렬됩니다. 그 외의 경우에는 에피소드가 출시일 순으로 내림차순 정렬됩니다.
     * @return EpisodeDTO 객체의 목록. 각 EpisodeDTO에는 에피소드에 대한 정보, TTS 파일, 스크립트 파일의 가용 여부, 그리고 코멘트 수가 포함되어 있습니다.
     */
    public List<EpisodeDTO> getEpisodes(String novelId, EpisodeSortOrder sortOrder) {
        Sort sort = Sort.by(Sort.Direction.DESC, "releasedDate");
        if (sortOrder == EpisodeSortOrder.oldest) {
            sort = Sort.by(Sort.Direction.ASC, "releasedDate");
        }

        List<Episode> episodes = novelRepository.findEpisodesByNovelId(novelId, sort);
        return episodes.stream()
            .map(episode -> {
                boolean ttsAvailable = fileMetaDataRepository.existsByEpisodeIdAndType(episode.getId(), FileType.TTS);
                boolean scriptAvailable = fileMetaDataRepository.existsByEpisodeIdAndType(episode.getId(), FileType.SCRIPT);
                int commentCount = commentRepository.countCommentsByEpisodeId(episode.getId());
                return EpisodeDTO.of(episode, ttsAvailable, scriptAvailable, commentCount);
            })
            .toList();
    }
}
