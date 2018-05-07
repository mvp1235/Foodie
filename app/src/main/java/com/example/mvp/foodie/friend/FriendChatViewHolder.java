package com.example.mvp.foodie.friend;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mvp.foodie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendChatViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout friendBlock;
    public CircleImageView friendPhoto;
    public AppCompatTextView friendName;


    public FriendChatViewHolder(View itemView) {
        super(itemView);
        friendBlock = itemView.findViewById(R.id.friendBlock_id);
        friendPhoto = itemView.findViewById(R.id.friendPhoto_id);
        friendName = itemView.findViewById(R.id.friendName_id);
    }
}
