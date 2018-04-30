package com.example.mvp.foodie.friend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.FriendRequest;

import java.util.List;

public class FriendRequestRecyclerAdapter extends RecyclerView.Adapter<FriendRequestViewHolder>{

    private Context context;
    private List<FriendRequest> requestList;

    public FriendRequestRecyclerAdapter(Context context, List<FriendRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item_layout, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        final FriendRequest friendRequest = requestList.get(position);
        if (friendRequest.getType().equals("sent")) {
            holder.cancelBtn.setVisibility(View.VISIBLE);
            holder.acceptBtn.setVisibility(View.INVISIBLE);
            holder.declineBtn.setVisibility(View.GONE);
        } else {
            holder.cancelBtn.setVisibility(View.GONE);
            holder.acceptBtn.setVisibility(View.VISIBLE);
            holder.declineBtn.setVisibility(View.VISIBLE);
        }

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest(friendRequest);
            }
        });

        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineRequest(friendRequest);
            }
        });

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequest(friendRequest);
            }
        });
    }

    private void acceptRequest(FriendRequest friendRequest) {
        Toast.makeText(context, "Accepted Request", Toast.LENGTH_SHORT).show();
    }

    private void declineRequest(FriendRequest friendRequest) {
        Toast.makeText(context, "Declined request", Toast.LENGTH_SHORT).show();
    }

    private void cancelRequest(FriendRequest friendRequest) {
        Toast.makeText(context, "Canceled request", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
