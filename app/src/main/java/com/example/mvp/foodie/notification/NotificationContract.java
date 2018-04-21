package com.example.mvp.foodie.notification;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Notification;

import java.util.List;

public interface NotificationContract {
    interface View {
        void onLoadSuccess(List<Notification> notifications);
        void onLoadFailure(String error);
    }

    interface Presenter {
        void loadNotifications(BaseActivity activity, String userID);
    }
}
