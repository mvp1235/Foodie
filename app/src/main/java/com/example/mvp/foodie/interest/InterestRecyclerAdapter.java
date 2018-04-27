package com.example.mvp.foodie.interest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Interest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InterestRecyclerAdapter extends RecyclerView.Adapter<InterestViewHolder> {

    private Context context;
    private List<Interest> interestList;

    public InterestRecyclerAdapter(Context context, List<Interest> interestList) {
        this.context = context;
        this.interestList = interestList;
    }

    public void addInterest(Interest interest) {
        interestList.add(interest);
        this.notifyDataSetChanged();
    }

    public void setInterestList(List<Interest> interestList) {
        this.interestList = interestList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.interest_item_layout, parent, false);

        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        final Interest interest = interestList.get(position);

        holder.userName.setText(interest.getUserName());
        Picasso.get().load(interest.getProfileURL()).into(holder.userProfile);
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }
}
