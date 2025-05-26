package com.example.demo.Model;

import jakarta.validation.constraints.NotEmpty;

import java.sql.Timestamp;

public class Follow {
    @NotEmpty
    private int followId,userIdSrc,userIdDst;
    private Timestamp time;

    public Follow() {
        this.followId =  -1;
    }

    @NotEmpty
    public int getFollowId() {
        return followId;
    }

    public void setFollowId(@NotEmpty int followId) {
        this.followId = followId;
    }

    @NotEmpty
    public int getUserIdSrc() {
        return userIdSrc;
    }

    public void setUserIdSrc(@NotEmpty int userIdSrc) {
        this.userIdSrc = userIdSrc;
    }

    @NotEmpty
    public int getUserIdDst() {
        return userIdDst;
    }

    public void setUserIdDst(@NotEmpty int userIdDst) {
        this.userIdDst = userIdDst;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
