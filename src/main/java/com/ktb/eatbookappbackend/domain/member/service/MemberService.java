package com.ktb.eatbookappbackend.domain.member.service;

import com.ktb.eatbookappbackend.domain.global.dto.PaginationWithDataDTO;
import com.ktb.eatbookappbackend.domain.bookmark.repository.BookmarkRepository;
import com.ktb.eatbookappbackend.domain.global.dto.PaginationInfoDTO;
import com.ktb.eatbookappbackend.domain.member.dto.MemberBookmarkedNovelDTO;
import com.ktb.eatbookappbackend.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final BookmarkRepository bookmarkRepository;

    public PaginationWithDataDTO<MemberBookmarkedNovelDTO> getMemberBookmarkedNovels(String memberId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByMemberIdWithNovel(memberId, pageRequest);

        PaginationInfoDTO paginationInfo = new PaginationInfoDTO(
                bookmarkPage.getNumber() + 1,
                bookmarkPage.getSize(),
                (int) bookmarkPage.getTotalElements(),
                bookmarkPage.getTotalPages()
        );

        List<MemberBookmarkedNovelDTO> bookmarks = bookmarkPage.getContent().stream()
                .map(bookmark -> MemberBookmarkedNovelDTO.of(bookmark.getNovel()))
                .collect(Collectors.toList());

        return PaginationWithDataDTO.of(paginationInfo, "novels", bookmarks);
    }
}
