package com.example.mvp.foodie.models;

import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_DAY;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_HOUR;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_MINUTE;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_MONTH;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_YEAR;

public class FriendRequest {
    private String toUserID;
    private String fromUserID;
    private String id;
    private long createdTime;
    private String type;

    public FriendRequest() {
        createdTime = System.currentTimeMillis();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getRequestDuration() {
        String time = "";
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - createdTime;

        //number of difference in seconds
        timeDifference /= 1000;

        long numYears = timeDifference / SECONDS_IN_YEAR;
        long numMonths = timeDifference / SECONDS_IN_MONTH;
        long numDays = timeDifference / SECONDS_IN_DAY;
        long numHours = timeDifference / SECONDS_IN_HOUR;
        long numMinutes = timeDifference / SECONDS_IN_MINUTE;

        if (numYears >= 1) {
            time = numYears + " yr ago";
        } else if (numMonths >= 1) {
            time = numMonths + " mo ago";
        } else if (numDays >= 1) {
            time = numDays + " day ago";
        } else if (numHours >= 1) {
            time = numHours + " hr ago";
        } else if (numMinutes >= 1) {
            time = numMinutes + " min ago";
        } else {
            time = "Just now";
        }
        return time;
    }
}
