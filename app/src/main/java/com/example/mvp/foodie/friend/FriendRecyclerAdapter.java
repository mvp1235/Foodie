package com.example.mvp.foodie.friend;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Friend;
import com.example.mvp.foodie.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mvp.foodie.UtilHelper.REQUEST_CODE;
import static com.example.mvp.foodie.UtilHelper.USER_ID;
import static com.example.mvp.foodie.UtilHelper.VIEW_OTHER_PROFILE;


public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.FriendViewHolder>{

    private Context context;
    private List<Friend> friends;
    private boolean currentUser;

    private FriendContract.Presenter friendPresenter;

    public FriendRecyclerAdapter(Context context, List<Friend> friends) {
        this.context = context;
        this.friends = friends;
        friendPresenter = new FriendPresenter((FriendListActivity)context);
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item_layout, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        final Friend friend = friends.get(position);
        holder.friendName.setText(friend.getFullName());
        Picasso.get().load(friend.getPhotoURL()).into(holder.friendPhoto);

        if (isCurrentUser()) {
            holder.unfriendBtn.setVisibility(View.VISIBLE);
            holder.unfriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friendPresenter.removeFriendship(FirebaseAuth.getInstance().getCurrentUser().getUid(), friend.getId());
                }
            });
        } else {
            holder.unfriendBtn.setVisibility(View.GONE);
        }

        holder.friendBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(REQUEST_CODE, VIEW_OTHER_PROFILE);
                intent.putExtra(USER_ID, friend.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public boolean isCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friends = friendList;
        notifyDataSetChanged();
    }

    public void addFriend(Friend friend) {
        if (!friends.contains(friend)) {
            friends.add(friend);
            notifyDataSetChanged();
        }
    }

    public void removeFriend(Friend friend) {
        if (friends.contains(friend)) {
            friends.remove(friend);
            notifyDataSetChanged();
        }
    }

    class FriendViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView friendPhoto;
        public AppCompatTextView friendName;
        public AppCompatButton unfriendBtn;
        public RelativeLayout friendBlock;

        public FriendViewHolder(View itemView) {
            super(itemView);
            friendPhoto = itemView.findViewById(R.id.friendPhoto_id);
            friendName = itemView.findViewById(R.id.friendName_id);
            unfriendBtn = itemView.findViewById(R.id.unfriendBtn_id);
            friendBlock = itemView.findViewById(R.id.friendBlock_id);
        }
    }
}
