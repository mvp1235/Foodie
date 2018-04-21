package com.example.mvp.foodie.notification;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationPresenter implements NotificationContract.Presenter {
    private NotificationContract.View view;

    public NotificationPresenter(NotificationContract.View view) {
        this.view = view;
    }


    @Override
    public void loadNotifications(BaseActivity activity, String userID) {
        DatabaseReference notificationRef = activity.getmDatabase().child("Notifications");
        notificationRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Notification> notifications = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification n = snapshot.getValue(Notification.class);
                    notifications.add(n);
                }

                if (notifications.size() > 0) {
                    Collections.reverse(notifications);
                    view.onLoadSuccess(notifications);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onLoadFailure(databaseError.getMessage());
            }
        });
    }

}
