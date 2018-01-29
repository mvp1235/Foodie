package com.example.mvp.foodie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

public class ViewHolder extends RecyclerView.ViewHolder{

    public android.support.v7.widget.AppCompatTextView name, location, time, description;
    public android.support.v7.widget.AppCompatImageView userProfile, postPhoto, postHeart;
    public LinearLayout likesLL, commentsLL;

    public ViewHolder(View itemView) {
        super(itemView);

        userProfile = itemView.findViewById(R.id.userProfilePhoto_id);
        name = itemView.findViewById(R.id.userName_id);
        location = itemView.findViewById(R.id.postLocation_id);
        time = itemView.findViewById(R.id.postTime_id);
        description = itemView.findViewById(R.id.postDescription_id);
        postPhoto = itemView.findViewById(R.id.postPhoto_id);
        postHeart = itemView.findViewById(R.id.postHeart_id);
        likesLL = itemView.findViewById(R.id.likesSection_id);
        commentsLL = itemView.findViewById(R.id.commentsSection_id);

    }
}
