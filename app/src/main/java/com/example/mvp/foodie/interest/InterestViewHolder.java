package com.example.mvp.foodie.interest;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mvp.foodie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class InterestViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView userProfile;
    public AppCompatTextView userName;

    public InterestViewHolder(View itemView) {
        super(itemView);

        userProfile = itemView.findViewById(R.id.interestUserPhoto_id);
        userName = itemView.findViewById(R.id.interestUserName_id);
    }
}
