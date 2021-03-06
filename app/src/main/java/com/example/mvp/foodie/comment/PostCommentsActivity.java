package com.example.mvp.foodie.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Comment;

import java.util.ArrayList;
import java.util.List;

import static com.example.mvp.foodie.UtilHelper.POST_ID;

public class PostCommentsActivity extends BaseActivity implements CommentContract.View, EditCommentDialogFragment.EditListener {

    Toolbar toolbar;
    private AppCompatEditText commentET;
    private AppCompatButton postBtn;

    private RecyclerView recyclerView;
    private CommentRecyclerAdapter adapter;

    private CommentPresenter presenter;

    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        initViews();
        setUpListeners();
    }

    private void initViews() {
        Intent intent = getIntent();
        String currentPostID = intent.getStringExtra(POST_ID);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.comments);

        recyclerView = findViewById(R.id.recyclerView_id);
        commentET = findViewById(R.id.commentText_id);
        postBtn = findViewById(R.id.postBtn_id);


        commentList = new ArrayList<>();
        adapter = new CommentRecyclerAdapter(this, commentList);
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
                    presenter.postComment(PostCommentsActivity.this, getIntent().getStringExtra(POST_ID), comment, getmAuth().getCurrentUser().getUid());
                }
            }
        });
    }

    @Override
    public void onCommentsLoadSuccess(Comment comment) {
        adapter.addComment(comment);
    }

    @Override
    public void onCommentsLoadFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentSuccess(Comment comment) {
        commentET.setText("");
        adapter.addComment(comment);
        recyclerView.scrollToPosition(commentList.size()-1);
    }

    @Override
    public void onCommentFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentEditSuccess(Comment comment) {
        adapter.replaceComment(comment);
    }

    @Override
    public void onCommentEditFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveClick(String commentText, String commentID) {
        presenter.editComment(this, commentID, commentText);
    }

}
