package com.example.mvp.foodie.models;

import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_DAY;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_HOUR;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_MINUTE;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_MONTH;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_YEAR;

public class Message {
    private String mID;
    private String fromUserID;
    private String toUserID;
    private String content;
    private long createdTime;

    public Message() {
        createdTime = System.currentTimeMillis();
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getToUserID() {
        return toUserID;
    }

    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
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

    public String getMessageDuration() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        Message other = (Message) obj;
        return this.mID.equals(other.mID);
    }
}
