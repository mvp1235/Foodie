package com.example.mvp.foodie.friend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.FriendRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendRequestRecyclerAdapter extends RecyclerView.Adapter<FriendRequestViewHolder> implements FriendContract.Adapter {

    private Context context;
    private List<FriendRequest> requestList;
    private FriendContract.Presenter presenter;

    public FriendRequestRecyclerAdapter(Context context, List<FriendRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
        presenter = new FriendPresenter((FriendRequestsActivity)context, this);
    }

    public void setRequestList(List<FriendRequest> requestList) {
        this.requestList = requestList;
        this.notifyDataSetChanged();
    }

    public void addRequest(FriendRequest request) {
        this.requestList.add(request);
        this.notifyDataSetChanged();
    }

    public void removeRequest(String fromUserID, String toUserID) {
        for (int i=0; i<requestList.size(); i++) {
            FriendRequest f = requestList.get(i);
            if (f.getToUserID().equals(toUserID) && f.getFromUserID().equals(fromUserID)) {
                requestList.remove(i);
                notifyDataSetChanged();
                break;
            }
        }

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
        holder.userFullName.setText(friendRequest.getToUserName());
        Picasso.get().load(friendRequest.getTargetPhotoURL()).into(holder.userPhoto);

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
            presenter.acceptFriendRequest(friendRequest.getFromUserID(), friendRequest.getToUserID());
            }
        });

        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            presenter.declineFriendRequest(friendRequest.getFromUserID(), friendRequest.getToUserID());
            }
        });

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            presenter.cancelFriendRequest(friendRequest.getFromUserID(), friendRequest.getToUserID());
            }
        });
    }



    @Override
    public int getItemCount() {
        return requestList.size();
    }

}
