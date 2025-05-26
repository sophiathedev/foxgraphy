package com.example.demo.Model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    static int cnt =0;
    @Id
    private int commentId;
    private int parentComment;
    private int postId;
    private int countVote;

    private int stateVote;
    private String username;
    private int userId;
    @NotEmpty
    private String content;
    private Timestamp time;

    public Comment(){
        this.parentComment=0;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStateVote() {
        return stateVote;
    }

    public void setStateVote(int stateVote) {
        this.stateVote = stateVote;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getParentComment() {
        return parentComment;
    }

    public void setParentComment(int parentComment) {
        this.parentComment = parentComment;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getCountVote() {
        return countVote;
    }

    public void setCountVote(int countVote) {
        this.countVote = countVote;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(this.time.toString().substring(0,19), originalFormatter);

        // Convert to UTC+7 (Asia/Bangkok)
        ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime utcPlus7DateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Asia/Bangkok"));

        // Format the new timestamp
        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, hh:mm a");
        return utcPlus7DateTime.format(newFormatter);
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
