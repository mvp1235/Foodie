package com.example.mvp.foodie.comment;

import android.app.Service;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.User;

import java.util.ArrayList;
import java.util.List;

public class PostCommentsActivity extends BaseActivity implements CommentContract.View {
    private AppCompatEditText commentET;
    private AppCompatButton postBtn;

    private RecyclerView recyclerView;
    private CommentRecyclerAdapter adapter;

    private CommentPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        initViews();
        setUpListeners();
    }

    private void initViews() {
        Intent intent = getIntent();
        String currentPostID = intent.getStringExtra("POST_ID");

        recyclerView = findViewById(R.id.recyclerView_id);
        commentET = findViewById(R.id.commentText_id);
        postBtn = findViewById(R.id.postBtn_id);


        adapter = new CommentRecyclerAdapter(this, new ArrayList<Comment>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);


        presenter = new CommentPresenter(this);
        presenter.loadComments(this, currentPostID);
    }

    private void setUpListeners() {
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentET.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    presenter.postComment(PostCommentsActivity.this, getIntent().getStringExtra("POST_ID"), comment, getmAuth().getCurrentUser().getUid());
                }
            }
        });
    }

    @Override
    public void onCommentsLoadSuccess(List<Comment> comments) {
        adapter.setComments(comments);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCommentsLoadFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentSuccess(List<Comment> comments) {
        commentET.setText("");
        adapter.setComments(comments);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(comments.size()-1);
    }

    @Override
    public void onCommentFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
