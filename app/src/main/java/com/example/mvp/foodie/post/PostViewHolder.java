package com.example.mvp.foodie.post;


import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mvp.foodie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder{

    public android.support.v7.widget.AppCompatTextView name, location, time, description, numInterests, numComments;
    public android.support.v7.widget.AppCompatImageView postPhoto, postHeart;
    public AppCompatImageButton menuBtn;
    public CircleImageView userProfile;
    public LinearLayout interestsLL, commentsLL;

    public PostViewHolder(View itemView) {
        super(itemView);

        userProfile = itemView.findViewById(R.id.userProfilePhoto_id);
        name = itemView.findViewById(R.id.userName_id);
        location = itemView.findViewById(R.id.postLocation_id);
        time = itemView.findViewById(R.id.postTime_id);
        description = itemView.findViewById(R.id.postDescription_id);
        menuBtn = itemView.findViewById(R.id.postMenu_id);

        numInterests = itemView.findViewById(R.id.postNumInterest_id);
        numComments = itemView.findViewById(R.id.postNumComments_id);

        postPhoto = itemView.findViewById(R.id.postPhoto_id);
        postHeart = itemView.findViewById(R.id.postInterests_id);
        interestsLL = itemView.findViewById(R.id.interestsSection);
        commentsLL = itemView.findViewById(R.id.commentsSection_id);

    }
}
