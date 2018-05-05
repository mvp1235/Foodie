package com.example.mvp.foodie.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.friend.FriendContract;
import com.example.mvp.foodie.friend.FriendPresenter;
import com.example.mvp.foodie.friend.FriendRecyclerAdapter;
import com.example.mvp.foodie.models.Friend;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatFriendListActivity extends AppCompatActivity implements FriendContract.FriendsView{

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private FriendRecyclerAdapter adapter;
    private List<Friend> friendList;
    private FriendContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friend_list);

        recyclerView = findViewById(R.id.recyclerView_id);

        friendList = new ArrayList<>();
        adapter = new FriendRecyclerAdapter(this, friendList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.friends);

        presenter = new FriendPresenter(this);
        presenter.loadFriends(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public void onLoadFriendsSuccess(Friend friend) {
        adapter.addFriend(friend);
    }

    @Override
    public void onLoadFriendFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
