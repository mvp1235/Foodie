package com.example.mvp.foodie.models;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private String cID;
    private String firstUserID;
    private String secondUserID;
    private long lastMessageTime;
    private List<Message> messages;

    public Conversation() {
        messages = new ArrayList<>();
        lastMessageTime = System.currentTimeMillis();
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void removeMessage(Message message) {
        if (messages.contains(message)) {
            messages.remove(message);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        Conversation other = (Conversation) obj;
        return other.cID.equals(this.cID);
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getFirstUserID() {
        return firstUserID;
    }

    public void setFirstUserID(String firstUserID) {
        this.firstUserID = firstUserID;
    }

    public String getSecondUserID() {
        return secondUserID;
    }

    public void setSecondUserID(String secondUserID) {
        this.secondUserID = secondUserID;
    }

    public List<Message> getMessages() {
        if (messages.size() > 100) {
            int lastIndex = messages.size();
            int firstIndex = lastIndex - 100;
            return messages.subList(firstIndex, lastIndex);
        }
        else
            return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
