package com.example.mvp.foodie.comment;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.User;

import java.util.List;

public class CommentPresenter implements CommentContract.Presenter, CommentContract.onPostListener, CommentContract.onLoadListener {
    private CommentContract.View view;
    private CommentContract.Interactor interactor;

    public CommentPresenter(CommentContract.View view) {
        this.view = view;
        interactor = new CommentInteractor(this, this);
    }

    @Override
    public void loadComments(BaseActivity activity, String postID) {
        interactor.loadCommentsFromFirebase(activity, postID);
    }

    @Override
    public void postComment(BaseActivity activity, String postID, String commentText, String userID) {
        interactor.postCommentToFirebase(activity, postID, commentText, userID);
    }

    @Override
    public void onPostSuccess(List<Comment> comments) {
        view.onCommentSuccess(comments);
    }

    @Override
    public void onPostFailure(String error) {
        view.onCommentFailure(error);
    }

    @Override
    public void onLoadSuccess(List<Comment> comments) {
        view.onCommentsLoadSuccess(comments);
    }

    @Override
    public void onLoadFailure(String error) {
        view.onCommentsLoadFailure(error);
    }

}
