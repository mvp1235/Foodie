package com.example.mvp.foodie.friend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    public void removeRequest(String sentUserID, String receivedUserID) {
        for (int i=0; i<requestList.size(); i++) {
            FriendRequest f = requestList.get(i);
            String sentID;
            String receivedID;

            if (f.getType().equals("semt")) {
                sentID = f.getSentUserID();
                receivedID = f.getReceivedUserID();
            } else {
                sentID = f.getReceivedUserID();
                receivedID = f.getSentUserID();
            }


            if (sentID.equals(sentUserID) && receivedID.equals(receivedUserID)) {
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
        String type = friendRequest.getType();

        if (type.equals("sent")) {
            holder.cancelBtn.setVisibility(View.VISIBLE);
            holder.acceptBtn.setVisibility(View.INVISIBLE);
            holder.declineBtn.setVisibility(View.GONE);

            holder.userFullName.setText(friendRequest.getReceivedUserName());
            Picasso.get().load(friendRequest.getTargetPhotoURL()).into(holder.userPhoto);

            holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.cancelFriendRequest(friendRequest.getSentUserID(), friendRequest.getReceivedUserID());
                }
            });

        } else {
            holder.cancelBtn.setVisibility(View.GONE);
            holder.acceptBtn.setVisibility(View.VISIBLE);
            holder.declineBtn.setVisibility(View.VISIBLE);

            holder.userFullName.setText(friendRequest.getSentUserName());
            Picasso.get().load(friendRequest.getTargetPhotoURL()).into(holder.userPhoto);

            holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.acceptFriendRequest(friendRequest.getReceivedUserID(), friendRequest.getSentUserID());
                }
            });

            holder.declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.declineFriendRequest(friendRequest.getReceivedUserID(), friendRequest.getSentUserID());
                }
            });

        }
    }



    @Override
    public int getItemCount() {
        return requestList.size();
    }

}
