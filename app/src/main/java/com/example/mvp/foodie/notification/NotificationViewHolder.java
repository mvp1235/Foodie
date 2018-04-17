package com.example.mvp.foodie.notification;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mvp.foodie.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    public android.support.v7.widget.AppCompatTextView userName, content, time;
    public CircleImageView photoURL;

    public NotificationViewHolder(View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.notificationUserName_id);
        content = itemView.findViewById(R.id.notificationContent_id);
        time = itemView.findViewById(R.id.notificationTime_id);
        photoURL = itemView.findViewById(R.id.notificationPhoto_id);
    }
}
