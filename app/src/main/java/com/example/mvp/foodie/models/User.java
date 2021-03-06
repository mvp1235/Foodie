package com.example.mvp.foodie.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uID;
    private String firstName;
    private String lastName;
    private String profileURL;
    private String email;
    private List<String> postIDs;
    private List<String> friendIDs;
    private List<String> tokenIDs;
    private List<String> pendingFriendRequestIDs;
    private List<String> sentFriendRequestIDs;
    private List<String> conversationIDs;


    public User(){
        postIDs = new ArrayList<>();
        friendIDs = new ArrayList<>();
        tokenIDs = new ArrayList<>();
        pendingFriendRequestIDs = new ArrayList<>();
        sentFriendRequestIDs = new ArrayList<>();
        conversationIDs = new ArrayList<>();
    };

    public void addConversationID(String conversationID) {
        conversationIDs.add(conversationID);
    }

    public void removeConversationID(String conversationID) {
        if (conversationIDs.contains(conversationID))
            conversationIDs.remove(conversationID);
    }

    public List<String> getConversationIDs() {
        return conversationIDs;
    }

    public void setConversationIDs(List<String> conversationIDs) {
        this.conversationIDs = conversationIDs;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPostCount() {
        return postIDs.size();
    }

    public int getFriendCount() {
        return friendIDs.size();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public List<String> getPostIDs() {
        return postIDs;
    }

    public void setPostIDs(List<String> postIDs) {
        this.postIDs = postIDs;
    }

    public void addPostID(String postID) {
        if (!postIDs.contains(postID))
            postIDs.add(postID);
    }

    public void deletePostID(String postID) {
        if(postIDs.contains(postID))
            postIDs.remove(postID);
    }

    public List<String> getFriendIDs() {
        return friendIDs;
    }

    public void setFriendIDs(List<String> friendIDs) {
        this.friendIDs = friendIDs;
    }

    public void addFriendID(String friendID) {
        if (!friendIDs.contains(friendID))
            friendIDs.add(friendID);
    }

    public void removeFriendID(String friendID) {
        if(friendIDs.contains(friendID))
            friendIDs.remove(friendID);
    }

    public List<String> getTokenIDs() {
        return tokenIDs;
    }

    public void setTokenIDs(List<String> tokenIDs) {
        this.tokenIDs = tokenIDs;
    }

    public void addTokenID(String tokenID) {
        if(!tokenIDs.contains(tokenID))
            tokenIDs.add(tokenID);
    }

    public void removeTokenID(String tokenID) {
        if (tokenIDs.contains(tokenID))
            tokenIDs.remove(tokenID);
    }

    public List<String> getPendingFriendRequestIDs() {
        return pendingFriendRequestIDs;
    }

    public void setPendingFriendRequestIDs(List<String> pendingFriendRequestIDs) {
        this.pendingFriendRequestIDs = pendingFriendRequestIDs;
    }

    public void addPendingFriendRequestID(String id) {
        if (!pendingFriendRequestIDs.contains(id))
            pendingFriendRequestIDs.add(id);
    }

    public void removePendingFriendRequestID(String id) {
        if (pendingFriendRequestIDs.contains(id))
            pendingFriendRequestIDs.remove(id);
    }

    public List<String> getSentFriendRequestIDs() {
        return sentFriendRequestIDs;
    }

    public void setSentFriendRequestIDs(List<String> sentFriendRequestIDs) {
        this.sentFriendRequestIDs = sentFriendRequestIDs;
    }

    public void addSentFriendRequestID(String id) {
        if (!sentFriendRequestIDs.contains(id))
            sentFriendRequestIDs.add(id);
    }

    public void removeSentFriendRequestID(String id) {
        if (sentFriendRequestIDs.contains(id))
            sentFriendRequestIDs.remove(id);
    }
}
