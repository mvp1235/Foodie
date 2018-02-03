package com.example.mvp.foodie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

public class ViewHolder extends RecyclerView.ViewHolder{

    public android.support.v7.widget.AppCompatTextView name, location, time, description, numGoing, numInterests, numComments;
    public android.support.v7.widget.AppCompatImageView userProfile, postPhoto, postHeart;
    public LinearLayout interestsLL, commentsLL, goingLL;

    public ViewHolder(View itemView) {
        super(itemView);

        userProfile = itemView.findViewById(R.id.userProfilePhoto_id);
        name = itemView.findViewById(R.id.userName_id);
        location = itemView.findViewById(R.id.postLocation_id);
        time = itemView.findViewById(R.id.postTime_id);
        description = itemView.findViewById(R.id.postDescription_id);

        numInterests = itemView.findViewById(R.id.postNumInterest_id);
        numGoing = itemView.findViewById(R.id.postNumGoing_id);
        numComments = itemView.findViewById(R.id.postNumComments_id);

        postPhoto = itemView.findViewById(R.id.postPhoto_id);
        postHeart = itemView.findViewById(R.id.postInterests_id);
        interestsLL = itemView.findViewById(R.id.interestsSection);
        commentsLL = itemView.findViewById(R.id.commentsSection_id);
        goingLL = itemView.findViewById(R.id.goingSection_id);

    }
}
