package com.example.mvp.foodie.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationList;

    public NotificationRecyclerAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.notifyDataSetChanged();
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

        setNotificationInfo(notification.getnID(), holder);
    }

    private void setNotificationInfo(String notificationID, final NotificationViewHolder holder) {
        DatabaseReference notificationRef = ((BaseActivity)context).getmDatabase().child("Notifications");

        notificationRef.child(notificationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Notification notification = dataSnapshot.getValue(Notification.class);
                if (notification != null) {
                    holder.content.setText(notification.getContent());
                    holder.userName.setText(notification.getUserName());
                    holder.time.setText(notification.getNotificationDuration());
                    Picasso.get().load(notification.getPhotoURL()).into(holder.photoURL);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
