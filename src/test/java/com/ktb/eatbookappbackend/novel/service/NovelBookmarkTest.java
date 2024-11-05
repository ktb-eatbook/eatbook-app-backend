package com.ktb.eatbookappbackend.novel.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.bookmark.fixture.BookmarkFixture;
import com.ktb.eatbookappbackend.domain.bookmark.repository.BookmarkRepository;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.novel.service.NovelService;
import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.member.fixture.MemberFixture;
import com.ktb.eatbookappbackend.novel.fixture.NovelFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NovelBookmarkTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private NovelRepository novelRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private NovelService novelService;

    private Member member;
    private Novel novel;

    @BeforeEach
    public void setUp() {
        member = MemberFixture.createMember();
        novel = NovelFixture.createNovel();

        when(memberService.findById(any(String.class))).thenReturn(member);
        when(novelRepository.findById(any(String.class))).thenReturn(Optional.of(novel));
    }

    @Test
    public void should_AddBookmark_When_NotAlreadyBookmarked() {
        // Given
        Bookmark expectedBookmark = BookmarkFixture.createBookmark(member, novel);

        when(bookmarkRepository.existsByNovelAndMember(novel, member)).thenReturn(false);
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(expectedBookmark);

        // When
        assertDoesNotThrow(() -> novelService.addBookmark(novel.getId(), member.getId()));

        // Then
        ArgumentCaptor<Bookmark> bookmarkCaptor = ArgumentCaptor.forClass(Bookmark.class);
        verify(bookmarkRepository, times(1)).save(bookmarkCaptor.capture());

        Bookmark resultBookmark = bookmarkCaptor.getValue();
        assertEquals(expectedBookmark.getNovel(), resultBookmark.getNovel());
        assertEquals(expectedBookmark.getMember(), resultBookmark.getMember());
    }

    @Test
    public void should_ThrowException_When_AlreadyBookmarked() {
        // Given
        when(bookmarkRepository.existsByNovelAndMember(novel, member)).thenReturn(true);

        // When
        NovelException exception = assertThrows(NovelException.class, () -> novelService.addBookmark(novel.getId(), member.getId()));

        // Then
        assertEquals(NovelErrorCode.ALREADY_BOOKMARKED, exception.getErrorCode());
        verify(bookmarkRepository, never()).save(any(Bookmark.class));
    }

    @Test
    public void should_DeleteBookmark_When_BookmarkExists() {
        // Given
        Bookmark bookmark = Bookmark.builder().novel(novel).member(member).build();

        when(bookmarkRepository.findByNovelAndMember(novel, member)).thenReturn(Optional.of(bookmark));

        // When
        assertDoesNotThrow(() -> novelService.deleteBookmark(novel.getId(), member.getId()));

        // Then
        verify(bookmarkRepository, times(1)).delete(bookmark);
    }

    @Test
    public void should_ThrowException_When_BookmarkForDeleteNotFound() {
        // Given
        when(bookmarkRepository.findByNovelAndMember(novel, member)).thenReturn(Optional.empty());

        // When
        NovelException exception = assertThrows(NovelException.class, () -> novelService.deleteBookmark(novel.getId(), member.getId()));

        // Then
        assertEquals(NovelErrorCode.BOOKMARK_NOT_FOUND, exception.getErrorCode());
        verify(bookmarkRepository, never()).delete(any(Bookmark.class));
    }
}
