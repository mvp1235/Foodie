package com.example.mvp.foodie.models;

import java.util.Calendar;

public class Comment {
    private String cID;
    private String content;
    private long createdTime;
    private User user;

    public Comment() {
        createdTime = Calendar.getInstance().getTimeInMillis();
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
