package com.example.mvp.foodie.signup;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public interface SignUpContract {
    interface View {
        void onSignUpSuccess(FirebaseUser user);
        void onSignUpFailure(String error);
    }

    interface Presenter {
        void signUp(Activity activity, String firstName, String lastName, String email, String password);
    }

    interface Interactor {
        void performFirebaseSignUp(Activity activity, String firstName, String lastName, String email, String password);
        void uploadCapturedPhotoToFirebase(Activity activity, Bitmap profileBitmap, String userID);
        void uploadGalleryPhotoToFirebase(Activity activity, Uri profileURI, String userID);
    }

    interface onRegistrationListener{
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(String message);
    }

    interface onUploadListener {
        void onPhotoUploadSuccess(Uri imageUri);
        void onPhotoUploadFailure(String error);
    }
}
