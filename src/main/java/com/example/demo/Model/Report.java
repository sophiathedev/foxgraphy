package com.example.demo.Model;

import jakarta.validation.constraints.NotEmpty;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Report {
    private int reportId;
    private int postId,commentId;
    @NotEmpty
    private String content,reason;
    private Timestamp time;
    public Report(){
        this.postId=-1;
        this.commentId=-1;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public @NotEmpty String getContent() {
        return content;
    }

    public void setContent(@NotEmpty String content) {
        this.content = content;
    }

    public @NotEmpty String getReason() {
        return reason;
    }

    public void setReason(@NotEmpty String reason) {
        this.reason = reason;
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

    public void setTime(@NotEmpty Timestamp time) {
        this.time = time;
    }
}
