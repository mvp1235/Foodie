package com.example.mvp.foodie.post;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Post;

public class PostPresenter implements PostContract.Presenter, PostContract.onPostCreateListener, PostContract.onLocationPickedListener, PostContract.onPostEditListener {

    private PostContract.View view;
    private PostContract.EditView editView;
    private PostContract.Interactor interactor;

    public PostPresenter(PostContract.View view) {
        this.view = view;
        interactor = new PostInteractor((PostContract.onPostCreateListener) this, this);
    }

    public PostPresenter(PostContract.EditView editView) {
        this.editView = editView;
        interactor = new PostInteractor((PostContract.onPostEditListener) this, this);
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
    public void editPost(BaseActivity activity, ImageView imageView, String description, String location, String userID, String postID) {
        interactor.editPostOnFirebase(activity, imageView, description, location, userID, postID);
    }

    @Override
    public void deletePost(BaseActivity activity, String postID) {
        interactor.deletePostOnFirebase(activity, postID);
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
        if (view != null)
            view.onLocationPickedSuccess(intent);
        if (editView != null)
            editView.onLocationPickedSuccess(intent);
    }

    @Override
    public void onLocationPickedFailure(String error) {
        if (view != null)
            view.onLocationPickedFailure(error);
        if (editView != null)
            editView.onLocationPickedFailure(error);
    }

    @Override
    public void onEditSuccess(Post post) {
        editView.onPostEditSuccess(post);
    }

    @Override
    public void onEditFailure(String error) {
        editView.onPostEditFailure(error);
    }
}
