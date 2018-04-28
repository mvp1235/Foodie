package com.example.mvp.foodie.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Post;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.USER_ID;

public class AllUserPostActivity extends BaseActivity implements PostContract.ListView {

    private RecyclerView recyclerView;
    private PostRecyclerAdapter adapter;

    private List<Post> posts;
    private PostContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_post);

        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        String userID = intent.getStringExtra(USER_ID);

        recyclerView = findViewById(R.id.recyclerView_id);
        posts = new ArrayList<>();

        adapter = new PostRecyclerAdapter(this, posts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        presenter = new PostPresenter(this);
        presenter.loadAllUserPosts(this, userID);
    }

    @Override
    public void onLoadPostSuccess(Post post) {
        adapter.addPost(post);
    }

    @Override
    public void onLoadPostFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
