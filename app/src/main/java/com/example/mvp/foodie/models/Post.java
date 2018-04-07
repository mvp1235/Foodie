package com.example.mvp.foodie.models;

import java.util.ArrayList;
import java.util.Calendar;

public class Post {
    private String description, time, photoURL, location;
    private long createdTime;
    private User user;
    private ArrayList<String> commentIDs;
    private ArrayList<String> interestIDs;
    private ArrayList<String> goingIDs;

    public Post() {
        createdTime = Calendar.getInstance().getTimeInMillis();
        commentIDs = new ArrayList<>();
        interestIDs = new ArrayList<>();
        goingIDs = new ArrayList<>();
    }

    public Post(User u) {
        user = u;
        description = "Best restaurant to go to during summer";
        time = "15m";
        photoURL = "https://i.ytimg.com/vi/mEBFswpYms4/maxresdefault.jpg";
        location = "San Jose, CA";

        commentIDs = new ArrayList<>();
        interestIDs = new ArrayList<>();
        goingIDs = new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getCommentIDs() {
        return commentIDs;
    }

    public void setCommentIDs(ArrayList<String> commentIDs) {
        this.commentIDs = commentIDs;
    }

    public void addCommentID(String commentID) {
        this.commentIDs.add(commentID);
    }

    public void removeCommentID(String commendID) {
        if (this.commentIDs.contains(commendID))
            this.commentIDs.remove(commendID);
    }

    public ArrayList<String> getInterestIDs() {
        return interestIDs;
    }

    public void setInterestIDs(ArrayList<String> interestIDs) {
        this.interestIDs = interestIDs;
    }

    public void addInterestID(String interestID) {
        this.interestIDs.add(interestID);
    }

    public void removeInterestID(String interestID) {
        if (this.interestIDs.contains(interestID))
            this.interestIDs.remove(interestID);
    }

    public ArrayList<String> getGoingIDs() {
        return goingIDs;
    }

    public void setGoingIDs(ArrayList<String> goingIDs) {
        this.goingIDs = goingIDs;
    }

    public void addGoingID(String goingID) {
        this.goingIDs.add(goingID);
    }

    public void removeGoingID(String goingID) {
        if (this.goingIDs.contains(goingID))
            this.goingIDs.remove(goingID);
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
