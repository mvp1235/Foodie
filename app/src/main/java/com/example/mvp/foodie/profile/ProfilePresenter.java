package com.example.mvp.foodie.profile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mvp.foodie.models.User;

public class ProfilePresenter implements ProfileContract.Presenter, ProfileContract.onEditListener, ProfileContract.onUploadListener, ProfileContract.onLoadListener {

    private ProfileContract.View view;
    private ProfileContract.Interactor interactor;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
        interactor = new ProfileInteractor(this, this, this);
    }


    @Override
    public void loadDataFromFirebase(String userID) {
        interactor.performLoadData(userID);
    }

    @Override
    public void edit(Activity activity, String userID, String firstName, String lastName, String email) {
        interactor.performProfileEdit(activity, userID, firstName, lastName, email);
    }


    @Override
    public void uploadCapturedPhoto(Activity activity, Bitmap profileBitmap, String userID) {
        interactor.uploadCapturedPhotoToFirebase(activity, profileBitmap, userID);
    }

    @Override
    public void uploadGalleryPhotoTo(Activity activity, Uri profileURI, String userID) {
        interactor.uploadGalleryPhotoToFirebase(activity, profileURI, userID);
    }


    @Override
    public void onEditSuccess(User user) {
        view.onEditSuccess(user);
    }

    @Override
    public void onEditFailure(String error) {

    }

    @Override
    public void onPhotoUploadSuccess(Uri imageUri) {
        view.onPhotoUploadSuccess(imageUri);
    }

    @Override
    public void onPhotoUploadFailure(String error) {
        view.onPhotoUploadFailure(error);
    }

    @Override
    public void onLoadDataSuccess(User user) {
        view.onLoadDataSuccess(user);
    }

    @Override
    public void onLoadDataFailure(String error) {
        view.onLoadDataFailure(error);
    }

}
