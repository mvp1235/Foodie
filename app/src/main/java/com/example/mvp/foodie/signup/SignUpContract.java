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
        void performUserDataStoring(Activity activity, String firstName, String lastName, String email);

    }

    interface onSignUpListener{
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(String message);
    }
}
