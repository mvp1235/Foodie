package com.example.mvp.foodie.friend;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mvp.foodie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView userPhoto;
    public AppCompatTextView userFullName;
    public AppCompatButton acceptBtn, declineBtn, cancelBtn;

    public FriendRequestViewHolder(View itemView) {
        super(itemView);

        userPhoto = itemView.findViewById(R.id.userPhoto_id);
        userFullName = itemView.findViewById(R.id.userFullName_id);
        acceptBtn = itemView.findViewById(R.id.acceptBtn_id);
        declineBtn = itemView.findViewById(R.id.declineBtn_id);
        cancelBtn = itemView.findViewById(R.id.cancelBtn_id);
    }
}
