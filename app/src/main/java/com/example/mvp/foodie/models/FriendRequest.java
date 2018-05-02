package com.example.mvp.foodie.models;

import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_DAY;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_HOUR;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_MINUTE;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_MONTH;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_YEAR;

public class FriendRequest {
    private String toUserID;
    private String toUserName;
    private String fromUserID;
    private String fromUserName;
    private String targetPhotoURL;
    private String type;

    public FriendRequest() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserID() {
        return toUserID;
    }

    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getTargetPhotoURL() {
        return targetPhotoURL;
    }

    public void setTargetPhotoURL(String targetPhotoURL) {
        this.targetPhotoURL = targetPhotoURL;
    }
}
