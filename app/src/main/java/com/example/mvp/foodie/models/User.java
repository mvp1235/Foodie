package com.example.mvp.foodie.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uID;
    private String firstName;
    private String lastName;
    private String profileURL;
    private String email;
    private int postCount;
    private int friendCount;
    private List<String> postIDs;
    private List<String> commentsIDs;

    public User(){
        postCount = 0;
        friendCount = 0;
        postIDs = new ArrayList<>();
        commentsIDs = new ArrayList<>();
    };

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public void incrementPostCount() {
        postCount++;
    }

    public void decrementPostCount() {
        postCount--;
    }

    public void incrementFriendCount() {
        friendCount++;
    }

    public void decrementFriendCount() {
        friendCount--;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public List<String> getPostIDs() {
        return postIDs;
    }

    public void setPostIDs(List<String> postIDs) {
        this.postIDs = postIDs;
    }

    public List<String> getCommentsIDs() {
        return commentsIDs;
    }

    public void setCommentsIDs(List<String> commentsIDs) {
        this.commentsIDs = commentsIDs;
    }
}
