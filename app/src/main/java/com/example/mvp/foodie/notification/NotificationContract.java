package com.example.mvp.foodie.notification;

import android.content.Intent;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Notification;

import java.util.List;

public interface NotificationContract {
    interface View {
        void onLoadSuccess(List<Notification> notifications);
        void onLoadFailure(String error);
    }

    interface Adapter {
        void onLoadNotificationSuccess(Notification notification, NotificationViewHolder holder);
        void onLoadNotificationFailure(String error);
        void onLoadDetailPostSuccess(Intent intent);
        void onLoadDetailPostFailure(String error);
    }

    interface Presenter {
        void loadNotifications(BaseActivity activity, String userID);
        void loadNotificationByID(BaseActivity activity, NotificationViewHolder holder, String notificationID, String toUserID);
        void loadDetailPostOfComment(BaseActivity activity, String postID);
    }
}
