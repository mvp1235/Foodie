package com.example.mvp.foodie.profile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mvp.foodie.models.User;

public interface ProfileContract {
    interface View {
        void onLoadDataSuccess(User user);
        void onLoadDataFailure(String error);
        void onEditSuccess(User user);
        void onEditFailure(String error);
        void onPhotoUploadSuccess(Uri profileURI);
        void onPhotoUploadFailure(String error);

    }

    interface Presenter {
        void loadDataFromFirebase(String userID);
        void edit(Activity activity, String userID, String firstName, String lastName, String email);
        void uploadCapturedPhoto(Activity activity, Bitmap profileBitmap, String userID);
        void uploadGalleryPhotoTo(Activity activity, Uri profileURI, String userID);
    }

    interface Interactor {
        void performLoadData(String userID);
        void performProfileEdit(Activity activity, String userID, String firstName, String lastName, String email);
        void uploadCapturedPhotoToFirebase(Activity activity, Bitmap profileBitmap, String userID);
        void uploadGalleryPhotoToFirebase(Activity activity, Uri profileURI, String userID);
    }

    interface onLoadListener {
        void onLoadDataSuccess(User user);
        void onLoadDataFailure(String error);
    }

    interface onEditListener{
        void onEditSuccess(User user);
        void onEditFailure(String error);
    }

    interface onUploadListener {
        void onPhotoUploadSuccess(Uri imageUri);
        void onPhotoUploadFailure(String error);
    }
}
