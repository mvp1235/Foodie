package com.example.mvp.foodie.models;

public class User {
    private String uID;
    private String firstName;
    private String lastName;
    private String profileURL;
    private String location;
    private String email;
    private int postCount;
    private int friendCount;

    public User(String uID) {
        this.uID = uID;
        firstName = "Joe";
        lastName = "Nguyen";
        profileURL = "https://www.shareicon.net/data/2016/09/01/822711_user_512x512.png";
        location = "San Jose, CA";
        postCount = 0;
        friendCount = 0;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
