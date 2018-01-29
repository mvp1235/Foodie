package com.example.mvp.foodie;

public class User {

    private String name;
    private String profileURL;

    public User() {
        name = "Huy Nguyen";
        profileURL = "https://www.shareicon.net/data/2016/09/01/822711_user_512x512.png";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
