package com.example.mvp.foodie.friend;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.FriendRequest;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsActivity extends BaseActivity implements FriendContract.View {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private FriendRequestRecyclerAdapter adapter;
    private List<FriendRequest> friendRequests;

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

        FriendRequest r = new FriendRequest();
        r.setType("sent");

        friendRequests.add(r);
        friendRequests.add(r);
        friendRequests.add(r);

        FriendRequest r2 = new FriendRequest();
        r2.setType("pending");
        friendRequests.add(r2);
        friendRequests.add(r2);

        adapter = new FriendRequestRecyclerAdapter(this, friendRequests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }


    @Override
    public void onSendRequestSuccess() {

    }

    @Override
    public void onSendRequestFailure(String error) {

    }

    @Override
    public void onCancelRequestSuccess() {

    }

    @Override
    public void onCancelRequestFailure(String error) {

    }

    @Override
    public void onAcceptRequestSuccess() {

    }

    @Override
    public void onAcceptRequestFailure(String error) {

    }
}
