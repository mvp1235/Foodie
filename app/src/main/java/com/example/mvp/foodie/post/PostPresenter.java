package com.example.mvp.foodie.post;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Post;

public class PostPresenter implements PostContract.Presenter, PostContract.onPostCreateListener, PostContract.onLocationPickedListener {

    private PostContract.View view;
    private PostContract.Interactor interactor;

    public PostPresenter(PostContract.View view) {
        this.view = view;
        interactor = new PostInteractor(this, this);
    }

    @Override
    public void getLocation(BaseActivity activity, String providedLocation) {
        interactor.performGetLocation(activity, providedLocation);
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

    @Override
    public void onLocationPickedSuccess(Intent intent) {
        view.onLocationPickedSuccess(intent);
    }

    @Override
    public void onLocationPickedFailure(String error) {
        view.onLocationPickedFailure(error);
    }
}
