package com.ktb.eatbookappbackend.member.service;

import com.ktb.eatbookappbackend.domain.bookmark.repository.BookmarkRepository;
import com.ktb.eatbookappbackend.domain.global.dto.PaginationInfoDTO;
import com.ktb.eatbookappbackend.domain.global.dto.PaginationWithDataDTO;
import com.ktb.eatbookappbackend.domain.member.dto.MemberBookmarkedNovelDTO;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.member.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private Novel novel;
    private Bookmark bookmark;
    private Page<Bookmark> bookmarkPage;

    @BeforeEach
    public void setUp() {
        member = MemberFixture.createMember();
        novel = MemberFixture.createNovel();
        bookmark = MemberFixture.createBookmark(novel, member);
    }

    @Test
    public void getMemberBookmarkedNovels_ValidInput_ReturnsExpectedBookmarks() {
        // Given
        String memberId = member.getId();
        int page = MemberFixture.PAGE;
        int size = MemberFixture.SIZE;
        int totalItems = MemberFixture.TOTAL_ITEMS;
        int totalPages = MemberFixture.TOTAL_PAGES;

        MemberBookmarkedNovelDTO expectedBookmarkDTO = new MemberBookmarkedNovelDTO(
                novel.getId(),
                novel.getTitle(),
                novel.getCoverImageUrl(),
                novel.getViewCount(),
                0,
                false
        );
        PaginationInfoDTO expectedPagination = new PaginationInfoDTO(page, size, totalItems, totalPages);
        PaginationWithDataDTO<MemberBookmarkedNovelDTO> expectedResult = PaginationWithDataDTO.of(expectedPagination, "novels", List.of(expectedBookmarkDTO));

        // When
        bookmarkPage = new PageImpl<>(Collections.singletonList(bookmark));
        when(bookmarkRepository.findByMemberIdWithNovel(any(), any(PageRequest.class))).thenReturn(bookmarkPage);
        PaginationWithDataDTO<MemberBookmarkedNovelDTO> result = memberService.getMemberBookmarkedNovels(memberId, page, size);

        // Then
        assertNotNull(result);
        assertAll(
                () -> assertEquals(expectedResult.pagination().currentPage(), result.pagination().currentPage()),
                () -> assertEquals(expectedResult.pagination().pageSize(), result.pagination().pageSize()),
                () -> assertEquals(expectedResult.pagination().totalItems(), result.pagination().totalItems()),
                () -> assertEquals(expectedResult.pagination().totalPages(), result.pagination().totalPages())
        );

        List<MemberBookmarkedNovelDTO> bookmarks = result.data().get("novels");
        assertNotNull(bookmarks);
        assertEquals(totalItems, bookmarks.size());

        MemberBookmarkedNovelDTO bookmarkDTO = bookmarks.get(0);
        assertEquals(novel.getId(), bookmarkDTO.id());
        assertEquals(novel.getTitle(), bookmarkDTO.title());
    }
    

    @Test
    public void getMemberBookmarkedNovels_NoBookmarks_ReturnsEmptyResult() {
        // Given
        String memberId = member.getId();
        int page = MemberFixture.PAGE;
        int size = MemberFixture.SIZE;
        int totalItems = MemberFixture.EMPTY_TOTAL_ITEMS;
        int totalPages = MemberFixture.EMPTY_TOTAL_PAGES;

        PaginationInfoDTO expectedPagination = new PaginationInfoDTO(page, size, totalItems, totalPages);
        PaginationWithDataDTO<MemberBookmarkedNovelDTO> expectedResult = PaginationWithDataDTO.of(expectedPagination, "novels", Collections.emptyList());

        // When
        when(bookmarkRepository.findByMemberIdWithNovel(any(), any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        PaginationWithDataDTO<MemberBookmarkedNovelDTO> result = memberService.getMemberBookmarkedNovels(memberId, page, size);

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