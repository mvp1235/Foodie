package com.example.mvp.foodie.notification;

import android.content.Intent;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.example.mvp.foodie.post.DetailPostActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.POST_ID;
import static com.example.mvp.foodie.UtilHelper.USER_ID;

public class NotificationPresenter implements NotificationContract.Presenter {
    private NotificationContract.View view;
    private NotificationContract.Adapter adapter;

    public NotificationPresenter(NotificationContract.View view) {
        this.view = view;
    }

    public NotificationPresenter(NotificationContract.Adapter adapter) {
        this.adapter = adapter;
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

    @Override
    public void loadNotificationByID(BaseActivity activity, final NotificationViewHolder holder, String notificationID, String toUserID) {
        DatabaseReference notificationRef = activity.getmDatabase().child("Notifications");

        notificationRef.child(toUserID).child(notificationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Notification notification = dataSnapshot.getValue(Notification.class);
                adapter.onLoadNotificationSuccess(notification, holder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                adapter.onLoadNotificationFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void loadDetailPostOfComment(BaseActivity activity, String postID) {
        final Intent intent = new Intent(((NotificationRecyclerAdapter)adapter).getContext(), DetailPostActivity.class);

        DatabaseReference postRef = activity.getmDatabase().child("Posts");
        final DatabaseReference userRef = activity.getmDatabase().child("Users");

        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Post post = dataSnapshot.getValue(Post.class);

                intent.putExtra(POST_ID, post.getPostID());
                intent.putExtra(USER_ID, post.getUserID());

                adapter.onLoadDetailPostSuccess(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
