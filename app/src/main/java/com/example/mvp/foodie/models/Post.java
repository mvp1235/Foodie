package com.example.mvp.foodie.models;

import java.util.ArrayList;
import java.util.Calendar;

public class Post {
    private String postID;
    private String description, time, photoURL, location;
    private long createdTime;
    private User user;
    private ArrayList<Comment> comments;
    private ArrayList<String> interestIDs;
    private ArrayList<String> goingIDs;

    public Post(){
        this.postID = postID;
        createdTime = Calendar.getInstance().getTimeInMillis();
        comments = new ArrayList<>();
        interestIDs = new ArrayList<>();
        goingIDs = new ArrayList<>();

        time = "Just now";
        photoURL = "https://i.ytimg.com/vi/mEBFswpYms4/maxresdefault.jpg";
        location = "San Jose, CA";

        this.user = new User();
    };

    public void updatePostDuration() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long timeDifference = currentTime - createdTime;

        //number of difference in seconds
        timeDifference /= 1000;

        int secondsInAYear = 31536000;
        int secondsInAMonth = 2592000;
        int secondsInADay = 86400;
        int secondsInAnHour = 3600;
        int secondsInAMinute = 60;

        long numYears = timeDifference / secondsInAYear;
        long numMonths = timeDifference / secondsInAMonth;
        long numDays = timeDifference / secondsInADay;
        long numHours = timeDifference / secondsInAnHour;
        long numMinutes = timeDifference / secondsInAMinute;

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
    }

    public String getCommentCount() {
        return comments.size() + " Comments";
    }

    public String getInterestCount() {
        return interestIDs.size() + " Interests";
    }

    public String getGoingCount() {
        return goingIDs.size() + " Going";
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        if (this.comments.contains(comment))
            this.comments.remove(comment);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
