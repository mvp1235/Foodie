package com.example.mvp.foodie.notification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationViewHolder> implements NotificationContract.Adapter {

    private Context context;
    private List<Notification> notificationList;
    private NotificationContract.Presenter presenter;

    public NotificationRecyclerAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        presenter = new NotificationPresenter(this);
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_layout, parent, false);

        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        final Notification notification = notificationList.get(position);

        presenter.loadNotificationByID((BaseActivity) context, holder, notification.getnID(), notification.getToUserID());

        holder.notificationBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = notification.getType();
                //if the notification is a like or comment, load the detail post page
                //else, it is a friend request or confirmation, lead to the friend requests page
                if (type.equals("comment") || type.equals("like"))
                    presenter.loadDetailPostOfComment((BaseActivity) context, notification.getPostID());
                else if (type.equals("friend request"))
                    presenter.loadFriendRequests((BaseActivity)context, notification.getToUserID());
                else if (type.equals("friend confirmation"))
                    presenter.loadFriendPage((BaseActivity)context, notification.getFromUserID());

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    @Override
    public void onLoadNotificationSuccess(Notification notification, NotificationViewHolder holder) {
        if (notification != null) {
            holder.content.setText(notification.getContent());
            holder.userName.setText(notification.getUserName());
            holder.time.setText(notification.getNotificationDuration());
            Picasso.get().load(notification.getPhotoURL()).into(holder.photoURL);
        }
    }

    @Override
    public void onLoadNotificationFailure(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadDetailPostSuccess(Intent intent) {
        context.startActivity(intent);
    }

    @Override
    public void onLoadDetailPostFailure(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadFriendRequestsSuccess(Intent intent) {
        context.startActivity(intent);
    }

    @Override
    public void onLoadFriendRequestsFailure(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadFriendPageSuccess(Intent intent) {
        context.startActivity(intent);
    }

    @Override
    public void onLoadFriendPageFailure(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }
}
