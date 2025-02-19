package com.ktb.eatbookappbackend.readingLog.service;

import com.ktb.eatbookappbackend.domain.readingLog.repository.ReadingLogRepository;
import com.ktb.eatbookappbackend.domain.readingLog.service.ReadingLogService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReadingLogServiceTest {

    @Mock
    private ReadingLogRepository readingLogRepository;

    @InjectMocks
    private ReadingLogService readingLogService;
}
