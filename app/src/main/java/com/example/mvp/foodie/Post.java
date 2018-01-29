package com.example.mvp.foodie;


import android.support.v7.widget.AppCompatImageView;

public class Post {
    private String description, time, photoURL, location;
    private User user;

    public Post(User u) {
        user = u;
        description = "Best restaurant to go to during summer";
        time = "15m";
        photoURL = "https://api.learn2crack.com/android/images/donut.png";
        location = "San Jose, CA";
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
}
