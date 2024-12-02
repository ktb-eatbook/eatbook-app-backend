package com.ktb.eatbookappbackend.domain.novel.service;

import com.ktb.eatbookappbackend.domain.novel.exception.NovelFileException;
import com.ktb.eatbookappbackend.domain.novel.message.NovelFileErrorCode;
import com.ktb.eatbookappbackend.entity.constant.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${presigned.url.expiration}")
    private int presignedUrlExpiration;

    /**
     * S3 파일 경로를 생성합니다.
     *
     * @param novelId   소설 ID
     * @param episodeId 에피소드 ID
     * @param fileType  파일 타입 (TTS, SCRIPT)
     * @param fileId    파일 ID
     * @return S3 파일 경로
     */
    private String buildS3Path(String novelId, String episodeId, FileType fileType, String fileId) {
        switch (fileType) {
            case TTS:
                return String.format("%s/%s/audio/%s.mp3", novelId, episodeId, fileId);
            case SCRIPT:
                return String.format("%s/%s/script/%s.txt", novelId, episodeId, fileId);
            default:
                throw new NovelFileException(NovelFileErrorCode.FILE_TYPE_NOT_FOUND);
        }
    }

    /**
     * 지정된 파일에 대한 pre-signed GET URL을 생성합니다.
     *
     * @param novelId   소설 ID
     * @param episodeId 에피소드 ID
     * @param fileType  파일 타입 (AUDIO, SCRIPT)
     * @param fileId    파일 ID (백오피스에서 제공된 ID)
     * @return pre-signed GET URL
     */
    public String generatePresignedGetUrl(String novelId, String episodeId, FileType fileType, String fileId) {
        try {
            String objectKey = buildS3Path(novelId, episodeId, fileType, fileId);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(builder -> builder
                .signatureDuration(Duration.ofMinutes(presignedUrlExpiration))
                .getObjectRequest(getObjectRequest)
            );

            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error("Presigned GET Url 생성 중 오류가 발생했습니다. novelId: {}, episodeId: {}, fileType: {}, fileId: {}", novelId, episodeId,
                fileType, fileId);
            throw new NovelFileException(NovelFileErrorCode.PRESIGNED_URL_GENERATION_FAILED);
        }
    }
}

