package com.example.mvp.foodie.post;

import android.app.Activity;
import android.widget.ImageView;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Post;

public class PostPresenter implements PostContract.Presenter, PostContract.onPostCreateListener{

    private PostContract.View view;
    private PostContract.Interactor interactor;

    public PostPresenter(PostContract.View view) {
        this.view = view;
        interactor = new PostInteractor(this);
    }

    @Override
    public void uploadPost(BaseActivity activity, ImageView imageView, String description, String location, String userID) {
        interactor.uploadPostToFirebaseStorage(activity, imageView, description, location, userID);
    }

    @Override
    public void onPostSuccess(Post post) {
        view.onPostCreated(post);
    }

    @Override
    public void onPostFailure(String error) {
        view.onPostFailed(error);
    }
}
