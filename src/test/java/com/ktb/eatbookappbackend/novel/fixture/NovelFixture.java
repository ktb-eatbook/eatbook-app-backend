package com.ktb.eatbookappbackend.novel.fixture;

import com.ktb.eatbookappbackend.entity.Novel;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class NovelFixture {

    public static final int FAVORITE_COUNT = 5;

    public static Novel createNovel() {
        Novel novel = Novel.builder()
            .title("Novel Title")
            .coverImageUrl("coverImageUrl")
            .summary("Novel Summary")
            .isCompleted(true)
            .build();
        String novelID = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(novel, "id", novelID);
        return novel;
    }

    public static Novel createNovelWithTitle(String title) {
        Novel novel = Novel.builder()
            .title(title)
            .coverImageUrl("coverImageUrl")
            .summary("Novel Summary")
            .isCompleted(true)
            .build();
        String novelID = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(novel, "id", novelID);
        return novel;
    }
}
