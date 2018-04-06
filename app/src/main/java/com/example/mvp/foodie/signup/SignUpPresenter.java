package com.example.mvp.foodie.signup;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public class SignUpPresenter implements SignUpContract.Presenter, SignUpContract.onSignUpListener, SignUpContract.onUploadListener {
    private SignUpContract.View signUpView;
    private SignUpContract.Interactor interactor;

    public SignUpPresenter(SignUpContract.View signUpView) {
        this.signUpView = signUpView;
        interactor = new SignUpInteractor(this, this);
    }

    @Override
    public void signUp(Activity activity, String firstName, String lastName, String email, String password) {
        interactor.performFirebaseSignUp(activity, firstName, lastName, email, password);
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
    public void storeUserData(Activity activity, String firstName, String lastName, String email) {
        interactor.performUserDataStoring(activity, firstName, lastName, email);
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        signUpView.onSignUpSuccess(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        signUpView.onSignUpFailure(message);
    }

    @Override
    public void onPhotoUploadSuccess(Uri imageUri) {
        signUpView.onPhotoUploadSuccess(imageUri);
    }

    @Override
    public void onPhotoUploadFailure(String error) {
        signUpView.onPhotoUploadFailure(error);
    }
}
