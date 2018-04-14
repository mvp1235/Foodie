package com.example.mvp.foodie.comment;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mvp.foodie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profilePhoto;
    public AppCompatTextView userName, commentText;
    public AppCompatImageButton menuBtn;

    public CommentViewHolder(View itemView) {
        super(itemView);

        profilePhoto = itemView.findViewById(R.id.userProfile_id);
        userName = itemView.findViewById(R.id.commentUserName_id);
        commentText = itemView.findViewById(R.id.commentText_id);
        menuBtn = itemView.findViewById(R.id.commentMenu_id);
    }
}
