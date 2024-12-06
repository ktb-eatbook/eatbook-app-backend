package com.ktb.eatbookappbackend.domain.episode.service;

import com.ktb.eatbookappbackend.entity.constant.FileType;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import com.ktb.eatbookappbackend.domain.novel.exception.NovelFileException;
import com.ktb.eatbookappbackend.domain.episode.message.EpisodeFileErrorCode;
import com.ktb.eatbookappbackend.entity.FileMetadata;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

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
     * S3 GetObjectRequest를 생성합니다. 파일 메타데이터를 기반으로 S3 GetObjectRequest를 생성합니다.
     * <p>
     * TTS(Text-To-Speech) 파일이 아닌 경우, 응답 컨텐츠 타입을 "text/plain; charset=utf-8"으로 설정합니다.
     *
     * @param fileMetadata 파일 메타데이터
     * @return 생성된 GetObjectRequest
     */
    private GetObjectRequest createGetObjectRequest(FileMetadata fileMetadata) {
        GetObjectRequest.Builder requestBuilder = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(fileMetadata.getPath());

        if (fileMetadata.getType() != FileType.TTS) {
            requestBuilder.responseContentType("text/plain; charset=utf-8");
        }

        return requestBuilder.build();
    }

    /**
     * 지정된 파일에 대한 pre-signed GET URL을 생성합니다.
     *
     * @param fileMetadata 파일 메타데이터 ID
     * @return pre-signed GET URL
     */
    public String generatePresignedGetUrl(FileMetadata fileMetadata) {
        try {
            GetObjectRequest getObjectRequest = createGetObjectRequest(fileMetadata);
            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(builder -> builder
                .signatureDuration(Duration.ofMinutes(presignedUrlExpiration))
                .getObjectRequest(getObjectRequest)
            );

            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error("Presigned GET Url 생성 중 오류가 발생했습니다. fileId: {}", fileMetadata.getId());
            throw new NovelFileException(EpisodeFileErrorCode.PRESIGNED_URL_GENERATION_FAILED);
        }
    }
}

