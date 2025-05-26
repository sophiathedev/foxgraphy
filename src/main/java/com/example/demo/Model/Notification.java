package com.example.demo.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Notification {
    private int id;
    private String message;
    private int postId;
    private int userId;
    private int state;
    private Timestamp time;
    public Notification(){};
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
