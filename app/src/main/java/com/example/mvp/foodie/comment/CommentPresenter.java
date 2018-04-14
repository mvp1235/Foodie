package com.example.mvp.foodie.comment;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.User;

import java.util.List;

public class CommentPresenter implements CommentContract.Presenter, CommentContract.onPostListener, CommentContract.onLoadListener, CommentContract.onEditListener {
    private CommentContract.View view;
    private CommentContract.Interactor interactor;

    public CommentPresenter(CommentContract.View view) {
        this.view = view;
        interactor = new CommentInteractor(this, this, this);
    }

    @Override
    public void editComment(BaseActivity activity, String commentID, String newContent) {
        interactor.editCommentOnFirebase(activity, commentID, newContent);
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
    public void onPostSuccess(Comment comment) {
        view.onCommentSuccess(comment);
    }

    @Override
    public void onPostFailure(String error) {
        view.onCommentFailure(error);
    }

    @Override
    public void onLoadSuccess(Comment comment) {
        view.onCommentsLoadSuccess(comment);
    }

    @Override
    public void onLoadFailure(String error) {
        view.onCommentsLoadFailure(error);
    }

    @Override
    public void onEditSuccess(Comment comment) {
        view.onCommentEditSuccess(comment);
    }

    @Override
    public void onEditFailure(String error) {
        view.onCommentEditFailure(error);
    }
}
