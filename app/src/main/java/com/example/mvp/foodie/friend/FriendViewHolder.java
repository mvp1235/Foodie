package com.example.mvp.foodie.friend;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mvp.foodie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView friendPhoto;
    public AppCompatTextView friendName;


    public FriendViewHolder(View itemView) {
        super(itemView);
        friendPhoto = itemView.findViewById(R.id.friendPhoto_id);
        friendName = itemView.findViewById(R.id.friendName_id);
    }
}
