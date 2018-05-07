package com.example.mvp.foodie.friend;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Friend;
import com.example.mvp.foodie.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.USER_ID;

public class FriendListActivity extends BaseActivity implements FriendContract.FriendsView {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private FriendRecyclerAdapter adapter;
    private List<Friend> friendList;

    private FriendContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        init();
    }

    private void init() {
        String userID = getIntent().getStringExtra(USER_ID);
        String currentUserID = getmAuth().getCurrentUser().getUid();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.friends);


        recyclerView = findViewById(R.id.recyclerView_id);

        friendList = new ArrayList<>();
        adapter = new FriendRecyclerAdapter(this, friendList);

        //Use for showing or hiding unfriend button on the adapter
        //Only show if user is viewing his/her own friend list, but not on others
        if (userID.equals(currentUserID))
            adapter.setCurrentUser(true);
        else
            adapter.setCurrentUser(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        presenter = new FriendPresenter(this);
        presenter.loadFriends(userID);

    }

    @Override
    public void onLoadFriendsSuccess(Friend friend) {
        adapter.addFriend(friend);
    }

    @Override
    public void onLoadFriendFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFriendshipSuccess(Friend friend) {
        adapter.removeFriend(friend);
        Toast.makeText(this, "You are no longer friend with " + friend.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFriendshipFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
