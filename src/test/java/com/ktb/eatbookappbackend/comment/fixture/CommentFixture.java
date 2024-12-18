package com.ktb.eatbookappbackend.comment.fixture;

import com.ktb.eatbookappbackend.entity.Comment;
import com.ktb.eatbookappbackend.entity.Episode;
import com.ktb.eatbookappbackend.entity.Member;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class CommentFixture {

    private static final String CONTENT = "테스트용 댓글 내용";

    public static Comment createComment(Episode episode, Member member) {
        Comment comment = Comment.builder()
            .content(CONTENT)
            .episode(episode)
            .member(member)
            .build();
        String commentID = UUID.randomUUID().toString();
        ReflectionTestUtils.setField(comment, "id", commentID);
        return comment;
    }
}
