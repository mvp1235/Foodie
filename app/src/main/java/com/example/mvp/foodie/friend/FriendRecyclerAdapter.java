package com.example.mvp.foodie.friend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Friend;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    private Context context;
    private List<Friend> friendList;

    public FriendRecyclerAdapter(Context context, List<Friend> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
        notifyDataSetChanged();
    }

    public void addFriend(Friend friend) {
        if (!friendList.contains(friend)) {
            friendList.add(friend);
            notifyDataSetChanged();
        }
    }

    public void removeFriend(Friend friend) {
        if (friendList.contains(friend)) {
            friendList.remove(friend);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item_layout, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.friendName.setText(friend.getFullName());
        Picasso.get().load(friend.getPhotoURL()).into(holder.friendPhoto);

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
