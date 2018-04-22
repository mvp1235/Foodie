package com.example.mvp.foodie.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.User;

public interface ProfileContract {
    interface View {
        void onLoadDataSuccess(User user);
        void onLoadDataFailure(String error);
        void onPhotoUploadSuccess(Uri profileURI);
        void onPhotoUploadFailure(String error);
    }

    interface EditView {
        void onEditSuccess(String successMessage, User user);
        void onEditFailure(String error);
        void onLoadSuccess(String firstName, String lastName, String email);
        void onLoadFailure(String error);
    }

    interface Presenter {
        void loadDataFromFirebase(String userID);
        void uploadCapturedPhoto(BaseActivity activity, Bitmap profileBitmap, String userID);
        void uploadGalleryPhotoTo(BaseActivity activity, Uri profileURI, String userID);
    }

    interface EditPresenter {
        void loadData(Intent intent);
        void updateUserProfile(BaseActivity activity, String firstName, String lastName, String currentEmail, String newEmail, String newPassword);

    }

}
