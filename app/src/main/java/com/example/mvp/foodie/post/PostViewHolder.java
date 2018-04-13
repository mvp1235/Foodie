package com.example.mvp.foodie.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.comment.CommentViewHolder;
import com.example.mvp.foodie.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder{

    public android.support.v7.widget.AppCompatTextView name, location, time, description, numGoing, numInterests, numComments;
    public android.support.v7.widget.AppCompatImageView postPhoto, postHeart;
    public CircleImageView userProfile;
    public LinearLayout interestsLL, commentsLL, goingLL;

    public PostViewHolder(View itemView) {
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
