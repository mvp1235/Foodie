package com.example.mvp.foodie.models;

import java.util.ArrayList;
import java.util.List;

public class NotificationList {
    private List<Notification> notificationList;

    public NotificationList(){
        notificationList = new ArrayList<>();
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public void addNotification(Notification n) {
        if(!notificationList.contains(n))
            notificationList.add(n);
    }

    public void removeNotification(Notification n) {
        if (notificationList.contains(n))
            notificationList.remove(n);
    }
}
