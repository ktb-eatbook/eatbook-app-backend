package com.ktb.eatbookappbackend.member.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.domain.bookmark.repository.BookmarkRepository;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.global.dto.PaginationInfoDTO;
import com.ktb.eatbookappbackend.domain.global.dto.PaginationWithDataDTO;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.member.fixture.MemberFixture;
import com.ktb.eatbookappbackend.novel.fixture.NovelFixture;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private Novel novel;
    private Bookmark bookmark;
    private Page<Bookmark> bookmarkPage;

    @BeforeEach
    public void setUp() {
        member = MemberFixture.createMember();
        novel = NovelFixture.createNovel();
        bookmark = MemberFixture.createBookmark(novel, member);
    }

    @Test
    public void should_ReturnBookmarks_When_ValidInput() {
        // Given
        String memberId = member.getId();
        int page = MemberFixture.PAGE;
        int size = MemberFixture.SIZE;
        int totalItems = MemberFixture.TOTAL_ITEMS;
        int totalPages = MemberFixture.TOTAL_PAGES;
        int favoriteCount = MemberFixture.FAVORITE_COUNT;

        NovelDTO expectedBookmarkDTO = NovelDTO.of(novel, favoriteCount);
        PaginationInfoDTO expectedPagination = PaginationInfoDTO.of(page, size, totalItems, totalPages);
        PaginationWithDataDTO<NovelDTO> expectedResult = PaginationWithDataDTO.of(expectedPagination, "novels",
            List.of(expectedBookmarkDTO));

        bookmarkPage = new PageImpl<>(Collections.singletonList(bookmark));
        when(bookmarkRepository.findByMemberIdWithNovel(any(), any(PageRequest.class))).thenReturn(bookmarkPage);
        when(favoriteRepository.countByNovelId(any())).thenReturn(favoriteCount);

        // When
        PaginationWithDataDTO<NovelDTO> result = memberService.getMemberBookmarkedNovels(memberId, page, size);

        // Then
        assertNotNull(result);
        assertAll(
            () -> assertEquals(expectedResult.pagination().currentPage(), result.pagination().currentPage()),
            () -> assertEquals(expectedResult.pagination().pageSize(), result.pagination().pageSize()),
            () -> assertEquals(expectedResult.pagination().totalItems(), result.pagination().totalItems()),
            () -> assertEquals(expectedResult.pagination().totalPages(), result.pagination().totalPages())
        );

        List<NovelDTO> bookmarks = result.data().get("novels");
        assertNotNull(bookmarks);
        assertEquals(totalItems, bookmarks.size());

        NovelDTO bookmarkDTO = bookmarks.get(0);
        assertAll(
            () -> assertEquals(novel.getId(), bookmarkDTO.id()),
            () -> assertEquals(novel.getTitle(), bookmarkDTO.title()),
            () -> assertEquals(favoriteCount, bookmarkDTO.favoriteCount())
        );
    }

    @Test
    public void should_ReturnEmptyResult_When_NoBookmarks() {
        // Given
        String memberId = member.getId();
        int page = MemberFixture.PAGE;
        int size = MemberFixture.SIZE;
        int totalItems = MemberFixture.EMPTY_TOTAL_ITEMS;
        int totalPages = MemberFixture.EMPTY_TOTAL_PAGES;

        PaginationInfoDTO expectedPagination = PaginationInfoDTO.of(page, size, totalItems, totalPages);
        PaginationWithDataDTO<NovelDTO> expectedResult = PaginationWithDataDTO.of(expectedPagination, "novels", Collections.emptyList());

        when(bookmarkRepository.findByMemberIdWithNovel(any(), any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        // When
        PaginationWithDataDTO<NovelDTO> result = memberService.getMemberBookmarkedNovels(memberId, page, size);

        // Then
        assertNotNull(result);
        assertAll(
            () -> assertEquals(expectedResult.pagination().currentPage(), result.pagination().currentPage()),
            () -> assertEquals(expectedResult.pagination().pageSize(), result.pagination().pageSize()),
            () -> assertEquals(expectedResult.pagination().totalItems(), result.pagination().totalItems()),
            () -> assertEquals(expectedResult.pagination().totalPages(), result.pagination().totalPages())
        );
        assertEquals(Collections.emptyList(), result.data().get("novels"));
    }
}