package com.ktb.eatbookappbackend.domain.member.service;

import com.ktb.eatbookappbackend.domain.bookmark.repository.BookmarkRepository;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.global.dto.PaginationInfoDTO;
import com.ktb.eatbookappbackend.domain.member.dto.BookmarkedNovelsPaginationDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Novel;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final BookmarkRepository bookmarkRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 특정 멤버가 북마크한 소설 목록을 페이지로 나누어 반환합니다.
     *
     * @param memberId 멤버의 고유 식별자.
     * @param page     검색할 페이지 번호.
     * @param size     페이지 당 항목 수.
     * @return {@link BookmarkedNovelsPaginationDTO} 페이지네이션 정보와 북마크된 소설 목록을 담고 있는 객체.
     */
    @Transactional(readOnly = true)
    public BookmarkedNovelsPaginationDTO getMemberBookmarkedNovels(String memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByMemberIdWithNovel(memberId, pageRequest);

        PaginationInfoDTO paginationInfo = PaginationInfoDTO.of(
            page,
            size,
            (int) bookmarkPage.getTotalElements(),
            bookmarkPage.getTotalPages()
        );

        List<NovelDTO> bookmarks = bookmarkPage.getContent().stream()
            .map(bookmark -> {
                Novel novel = bookmark.getNovel();
                int favoriteCount = favoriteRepository.countByNovelId(novel.getId());
                return NovelDTO.of(bookmark.getNovel(), favoriteCount);
            })
            .collect(Collectors.toList());

        return BookmarkedNovelsPaginationDTO.of(paginationInfo, bookmarks);
    }
}
