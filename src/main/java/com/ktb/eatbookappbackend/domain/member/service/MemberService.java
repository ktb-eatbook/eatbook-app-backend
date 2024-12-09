package com.ktb.eatbookappbackend.domain.member.service;

import static com.ktb.eatbookappbackend.global.util.ValidationUtil.validatePageIndex;

import com.ktb.eatbookappbackend.domain.bookmark.repository.BookmarkRepository;
import com.ktb.eatbookappbackend.domain.favorite.repository.FavoriteRepository;
import com.ktb.eatbookappbackend.domain.member.dto.BookmarkedNovelsPaginationDTO;
import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.novel.dto.NovelDTO;
import com.ktb.eatbookappbackend.entity.Bookmark;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.global.dto.PaginationInfoDTO;
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

    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 이 메서드는 {@code memberId}로 전달된 멤버를 데이터베이스에서 검색합니다. 멤버가 존재하지 않으면 {@link MemberException}을 발생시킵니다.
     *
     * @param memberId 찾을 멤버의 고유 식별자.
     * @return 찾은 멤버.
     * @throws MemberException {@code memberId}로 지정된 멤버가 존재하지 않을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public Member findById(String memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 이 메서드는 {@code memberId}로 전달된 멤버를 데이터베이스에서 검색합니다. 멤버를 Soft-Delete 처리한 다음, 변경 사항을 데이터베이스에 저장합니다.
     *
     * @param memberId 삭제할 멤버의 고유 식별자.
     * @throws MemberException {@code memberId}로 지정된 멤버가 존재하지 않을 경우 발생합니다.
     */
    @Transactional
    public void deleteMember(String memberId) {
        Member member = findById(memberId);
        member.delete();
        memberRepository.save(member);
    }

    /**
     * 특정 멤버가 북마크한 소설 목록을 페이지로 나누어 반환합니다.
     *
     * @param memberId 멤버의 고유 식별자.
     * @param page     검색할 페이지 번호. 사용자 요청 페이지는 1 기반, 내부적으로는 0 기반으로 변환.
     * @param size     페이지 당 항목 수.
     * @return {@link BookmarkedNovelsPaginationDTO} 페이지네이션 정보와 북마크된 소설 목록을 담고 있는 객체.
     */
    @Transactional(readOnly = true)
    public BookmarkedNovelsPaginationDTO getMemberBookmarkedNovels(String memberId, int page, int size) {
        int actualPageIndex = page - 1;
        PageRequest pageRequest = PageRequest.of(actualPageIndex, size);
        Page<Bookmark> bookmarkPage = bookmarkRepository.findByMemberIdWithNovel(memberId, pageRequest);
        validatePageIndex(actualPageIndex, bookmarkPage.getTotalPages());

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
