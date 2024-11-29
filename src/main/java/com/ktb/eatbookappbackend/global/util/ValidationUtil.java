package com.ktb.eatbookappbackend.global.util;

import com.ktb.eatbookappbackend.global.exception.GlobalException;
import com.ktb.eatbookappbackend.global.message.GlobalErrorMessage;

public class ValidationUtil {

    /**
     * 주어진 페이지 인덱스가 전체 페이지 수에 기반하여 유효한 범위 내에 있는지 검증합니다.
     *
     * @param pageIndex  검증할 현재 페이지 인덱스 (0부터 시작).
     * @param totalPages 사용 가능한 전체 페이지 수.
     * @throws GlobalException 페이지 인덱스가 최대 유효 페이지 인덱스보다 큰 경우 발생합니다.
     */
    public static void validatePageIndex(int pageIndex, int totalPages) {
        int maxValidPageIndex = totalPages - 1;
        if (totalPages > 0 && pageIndex > maxValidPageIndex) {
            throw new GlobalException(GlobalErrorMessage.INVALID_QUERY_PARAMETER);
        }
    }
}