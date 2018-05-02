package com.example.mvp.foodie.models;

public class FriendRequest {
    private String receivedUserID;
    private String receivedUserName;
    private String sentUserID;
    private String sentUserName;
    private String targetPhotoURL;
    private String type;

    public FriendRequest() {}

    public String getReceivedUserID() {
        return receivedUserID;
    }

    public void setReceivedUserID(String receivedUserID) {
        this.receivedUserID = receivedUserID;
    }

    public String getReceivedUserName() {
        return receivedUserName;
    }

    public void setReceivedUserName(String receivedUserName) {
        this.receivedUserName = receivedUserName;
    }

    public String getSentUserID() {
        return sentUserID;
    }

    public void setSentUserID(String sentUserID) {
        this.sentUserID = sentUserID;
    }

    public String getSentUserName() {
        return sentUserName;
    }

    public void setSentUserName(String sentUserName) {
        this.sentUserName = sentUserName;
    }

    public String getTargetPhotoURL() {
        return targetPhotoURL;
    }

    public void setTargetPhotoURL(String targetPhotoURL) {
        this.targetPhotoURL = targetPhotoURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
