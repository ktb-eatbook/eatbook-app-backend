package com.ktb.eatbookappbackend.novel.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.member.service.MemberService;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.novel.service.NovelService;
import com.ktb.eatbookappbackend.entity.Favorite;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.favorite.fixture.FavoriteFixture;
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
public class NovelFavoriteTest {

    @Mock
    private FavoriteRepository favoriteRepository;

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
    public void should_AddFavorite_When_NotAlreadyFavoriteNovel() {
        // Given
        Favorite expectedFavorite = FavoriteFixture.createFavorite(member, novel);

        when(favoriteRepository.existsByNovelAndMember(novel, member)).thenReturn(false);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(expectedFavorite);

        // When
        assertDoesNotThrow(() -> novelService.addFavorite(novel.getId(), member.getId()));

        // Then
        ArgumentCaptor<Favorite> favoriteCaptor = ArgumentCaptor.forClass(Favorite.class);
        verify(favoriteRepository, times(1)).save(favoriteCaptor.capture());

        Favorite resultFavorite = favoriteCaptor.getValue();
        assertEquals(expectedFavorite.getNovel(), resultFavorite.getNovel());
        assertEquals(expectedFavorite.getMember(), resultFavorite.getMember());
    }

    @Test
    public void should_ThrowException_When_AlreadyFavoriteNovel() {
        // Given
        when(favoriteRepository.existsByNovelAndMember(novel, member)).thenReturn(true);

        // When
        NovelException exception = assertThrows(NovelException.class, () -> novelService.addFavorite(novel.getId(), member.getId()));

        // Then
        assertEquals(NovelErrorCode.ALREADY_FAVORITE, exception.getErrorCode());
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }

    @Test
    public void should_DeleteFavorite_When_FavoriteExists() {
        // Given
        Favorite favorite = FavoriteFixture.createFavorite(member, novel);

        when(favoriteRepository.findByNovelAndMember(novel, member)).thenReturn(Optional.of(favorite));

        // When
        assertDoesNotThrow(() -> novelService.deleteFavorite(novel.getId(), member.getId()));

        // Then
        verify(favoriteRepository, times(1)).delete(favorite);
    }

    @Test
    public void should_ThrowException_When_FavoriteForDeleteNotFound() {
        // Given
        when(favoriteRepository.findByNovelAndMember(novel, member)).thenReturn(Optional.empty());

        // When
        NovelException exception = assertThrows(NovelException.class, () -> novelService.deleteFavorite(novel.getId(), member.getId()));

        // Then
        assertEquals(NovelErrorCode.FAVORITE_NOT_FOUND, exception.getErrorCode());
        verify(favoriteRepository, never()).delete(any(Favorite.class));
    }
}
