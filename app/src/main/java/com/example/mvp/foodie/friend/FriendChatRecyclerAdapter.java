package com.example.mvp.foodie.friend;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.message.ChatActivity;
import com.example.mvp.foodie.models.Friend;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.mvp.foodie.UtilHelper.TO_USER_ID;
import static com.example.mvp.foodie.UtilHelper.USER_NAME;

public class FriendChatRecyclerAdapter extends RecyclerView.Adapter<FriendChatViewHolder> {
    private Context context;
    private List<Friend> friendList;

    public FriendChatRecyclerAdapter(Context context, List<Friend> friendList) {
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
    public FriendChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_chat_item_layout, parent, false);
        return new FriendChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendChatViewHolder holder, int position) {
        final Friend friend = friendList.get(position);
        holder.friendName.setText(friend.getFullName());
        Picasso.get().load(friend.getPhotoURL()).into(holder.friendPhoto);

        holder.friendBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(USER_NAME, friend.getFullName());
            intent.putExtra(TO_USER_ID, friend.getId());
            context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
