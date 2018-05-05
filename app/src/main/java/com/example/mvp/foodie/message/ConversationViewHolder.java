package com.example.mvp.foodie.message;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mvp.foodie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout conversationBlock;
    public CircleImageView userPhoto;
    public AppCompatTextView userName, lastMessageContent, lastMessageTime;

    public ConversationViewHolder(View itemView) {
        super(itemView);

        conversationBlock = itemView.findViewById(R.id.conversationBlock_id);
        userPhoto = itemView.findViewById(R.id.conversationUserPhoto_id);
        userName = itemView.findViewById(R.id.conversationUserName_id);
        lastMessageContent = itemView.findViewById(R.id.lastMessageContent_id);
        lastMessageTime = itemView.findViewById(R.id.lastMessageTime_id);
    }
}
