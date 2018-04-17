package com.example.mvp.foodie.models;

import java.util.ArrayList;

import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_DAY;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_HOUR;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_MINUTE;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_MONTH;
import static com.example.mvp.foodie.UtilHelper.SECONDS_IN_YEAR;

public class Post {
    private String postID;
    private String description, photoURL, location;
    private long createdTime;
    private String userID;
    private ArrayList<String> commentIDs;
    private ArrayList<String> interestIDs;
    private ArrayList<String> goingIDs;
    private ArrayList<String> subscribedTokenIDs;

    public Post(){
        this.postID = postID;
        createdTime = System.currentTimeMillis();
        commentIDs = new ArrayList<>();
        interestIDs = new ArrayList<>();
        goingIDs = new ArrayList<>();
        subscribedTokenIDs = new ArrayList<>();

        photoURL = "https://i.ytimg.com/vi/mEBFswpYms4/maxresdefault.jpg";
        location = "San Jose, CA";

    };

    public String getPostDuration() {
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

    public String getCommentCount() {
        return commentIDs.size() + "";
    }

    public String getInterestCount() {
        return interestIDs.size() + "";
    }

    public String getGoingCount() {
        return goingIDs.size() + "";
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public ArrayList<String> getCommentIDs() {
        return commentIDs;
    }

    public void setComments(ArrayList<String> commentIDs) {
        this.commentIDs = commentIDs;
    }

    public void addCommentID(String commentID) {
        this.commentIDs.add(commentID);
    }

    public void removeCommentID(String commentID) {
        if (this.commentIDs.contains(commentID))
            this.commentIDs.remove(commentID);
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

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setCommentIDs(ArrayList<String> commentIDs) {
        this.commentIDs = commentIDs;
    }

    public ArrayList<String> getSubscribedTokenIDs() {
        return subscribedTokenIDs;
    }

    public void setSubscribedTokenIDs(ArrayList<String> subscribedTokenIDs) {
        this.subscribedTokenIDs = subscribedTokenIDs;
    }

    public void addTokenID(String tokenID) {
        if (!subscribedTokenIDs.contains(tokenID))
            subscribedTokenIDs.add(tokenID);
    }

    public void removeTokenID(String tokenID) {
        if (subscribedTokenIDs.contains(tokenID))
            subscribedTokenIDs.remove(tokenID);
    }
}
