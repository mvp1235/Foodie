package com.example.mvp.foodie.friend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.FriendRequest;
import com.example.mvp.foodie.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.USER_ID;

public class FriendRequestsActivity extends BaseActivity implements FriendContract.View {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private FriendRequestRecyclerAdapter adapter;
    private List<FriendRequest> friendRequests;

    private FriendContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.friend_requests);

        recyclerView = findViewById(R.id.recyclerView_id);
        friendRequests = new ArrayList<>();

        adapter = new FriendRequestRecyclerAdapter(this, friendRequests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        Intent receivedIntent = getIntent();
        String userID = receivedIntent.getStringExtra(USER_ID);

        presenter = new FriendPresenter(this, adapter);
        if (userID == null)
            presenter.loadFriendRequests(getmAuth().getCurrentUser().getUid());
        else
            presenter.loadFriendRequests(userID);
    }


    @Override
    public void onCheckUserFriendshipSuccess(boolean friend) {

    }

    @Override
    public void onCheckUserFriendshipFailure(String error) {
        
    }

    @Override
    public void onCheckSentRequest(boolean sent) {

    }

    @Override
    public void onCheckSentFailure(String error) {

    }

    @Override
    public void onSendRequestSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "Sent a friend request to " + toUser.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendRequestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelRequestSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "Canceled friend request from " + toUser.getFullName(), Toast.LENGTH_SHORT).show();
        adapter.removeRequest(fromUser.getuID(), toUser.getuID());
    }

    @Override
    public void onCancelRequestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptRequestSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "You and " + toUser.getFullName() + " have became friends.", Toast.LENGTH_SHORT).show();
        adapter.removeRequest(fromUser.getuID(), toUser.getuID());
    }

    @Override
    public void onAcceptRequestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineRequestSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "You have declined friend request from " + toUser.getFullName(), Toast.LENGTH_SHORT).show();
        adapter.removeRequest(fromUser.getuID(), toUser.getuID());
    }

    @Override
    public void onDeclineRequestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadRequestsSuccess(FriendRequest friendRequest) {
        adapter.addRequest(friendRequest);
    }

    @Override
    public void onLoadRequestsFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFriendshipSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "You are no longer friend with " + toUser.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFriendshipFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
