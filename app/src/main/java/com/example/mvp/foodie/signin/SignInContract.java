package com.example.mvp.foodie.signin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public interface SignInContract {
    interface View {
        void onSignInSuccess(FirebaseUser user);
        void onSignInFailure(String error);

    }

    interface Presenter {
        void signIn(Activity activity, String email, String password);
    }

    interface Interactor {
        void performFirebaseSignIn(Activity activity, String email, String password);
    }

    interface onSignInListener{
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(String message);
    }
}
