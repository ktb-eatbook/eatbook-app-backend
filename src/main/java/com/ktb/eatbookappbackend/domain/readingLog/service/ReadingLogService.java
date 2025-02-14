package com.ktb.eatbookappbackend.domain.readingLog.service;

import com.ktb.eatbookappbackend.domain.episode.exception.EpisodeException;
import com.ktb.eatbookappbackend.domain.episode.message.EpisodeErrorCode;
import com.ktb.eatbookappbackend.domain.episode.repository.EpisodeRepository;
import com.ktb.eatbookappbackend.domain.member.exception.MemberException;
import com.ktb.eatbookappbackend.domain.member.message.MemberErrorCode;
import com.ktb.eatbookappbackend.domain.member.repository.MemberRepository;
import com.ktb.eatbookappbackend.domain.novel.dto.ContinueReadingInfoDTO;
import com.ktb.eatbookappbackend.domain.novel.dto.LastReadEpisodeDTO;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelErrorCode;
import com.ktb.eatbookappbackend.domain.novel.repository.NovelRepository;
import com.ktb.eatbookappbackend.domain.readingLog.dto.ReadingLogDTO;
import com.ktb.eatbookappbackend.domain.readingLog.repository.ReadingLogRepository;
import java.util.Optional;

import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Member;
import com.ktb.eatbookappbackend.entity.Novel;
import com.ktb.eatbookappbackend.entity.ReadingLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReadingLogService {

    private final ReadingLogRepository readingLogRepository;
    private final MemberRepository memberRepository;
    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    /**
     * 특정 멤버와 소설에 대한 마지막으로 읽은 에피소드 정보를 가져옵니다.
     *
     * @param memberId 멤버의 고유 식별자.
     * @param novelId  소설의 고유 식별자.
     * @return 마지막으로 읽은 에피소드에 대한 {@link LastReadEpisodeDTO}를 포함하는 {@link Optional}. 마지막으로 읽은 에피소드가 없는 경우, {@link Optional}은 비어 있습니다.
     */
    @Transactional(readOnly = true)
    public Optional<ContinueReadingInfoDTO> getLastReadEpisode(String memberId, String novelId) {
        return readingLogRepository.findLastReadEpisode(memberId, novelId)
                .map(readingLog -> LastReadEpisodeDTO.of(
                        readingLog.getEpisode().getId(),
                        readingLog.getEpisode().getChapterNumber(),
                        readingLog.getEpisode().getTitle(),
                        readingLog.getPageNumber(),
                        readingLog.getTtsLastPositionSeconds().toString()
                ))
                .map(ContinueReadingInfoDTO::of);
    }


    /**
     * 새로운 읽기 기록(ReadingLog)을 생성합니다.
     * 이 메서드는 주어진 사용자 ID와 요청 데이터를 기반으로, 특정 멤버가 읽고 있는 소설과
     * 특정 에피소드에 대한 읽기 기록을 저장합니다.
     *
     * @param memberId  읽기 기록을 저장할 멤버의 ID.
     * @param requestDTO 읽기 기록의 상세 정보를 담은 DTO 객체.
     * @throws MemberException  해당 멤버가 존재하지 않을 경우 발생.
     * @throws NovelException   해당 소설이 존재하지 않을 경우 발생.
     * @throws EpisodeException 해당 에피소드가 존재하지 않을 경우 발생.
     */
    @Transactional
    public void createReadingLog(String memberId, ReadingLogDTO requestDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Novel novel = novelRepository.findById(requestDTO.novelId())
                .orElseThrow(() -> new NovelException(NovelErrorCode.NOVEL_NOT_FOUND));

        Episode episode = episodeRepository.findById(requestDTO.episodeId())
                .orElseThrow(() -> new EpisodeException(EpisodeErrorCode.EPISODE_NOT_FOUND));

        ReadingLog readingLog = requestDTO.toEntity(member, novel, episode);
        readingLogRepository.save(readingLog);
    }
}
