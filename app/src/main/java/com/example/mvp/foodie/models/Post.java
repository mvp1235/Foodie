package com.example.mvp.foodie.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private ArrayList<String> subscribedUserIDs;

    //Used for notifications
    private ArrayList<String> subscribedTokenIDs;

    public Post(){
        this.postID = postID;
        createdTime = System.currentTimeMillis();
        commentIDs = new ArrayList<>();
        interestIDs = new ArrayList<>();
        subscribedTokenIDs = new ArrayList<>();
        subscribedUserIDs = new ArrayList<>();

    };

    public ArrayList<String> getSubscribedUserIDs() {
        return subscribedUserIDs;
    }

    public void setSubscribedUserIDs(ArrayList<String> subscribedUserIDs) {
        this.subscribedUserIDs = subscribedUserIDs;
    }

    public void addSubscriberID(String userID) {
        if (!subscribedUserIDs.contains(userID))
            subscribedUserIDs.add(userID);
    }

    public void removeSubscriberID(String userID) {
        if (subscribedUserIDs.contains(userID))
            subscribedUserIDs.remove(userID);
    }

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

        if (numYears >= 1)
            time = numYears + " yr ago";
        else if (numMonths >= 1)
            time = numMonths + " mo ago";
        else if (numDays >= 1)
            time = numDays + " day ago";
        else if (numHours >= 1)
            time = numHours + " hr ago";
        else if (numMinutes >= 1)
            time = numMinutes + " min ago";
        else
            time = "Just now";

        return time;
    }

    public String getCommentCount() {
        return commentIDs.size() + " Comments";
    }

    public String getInterestCount() {
        return interestIDs.size() + " Interests";
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
