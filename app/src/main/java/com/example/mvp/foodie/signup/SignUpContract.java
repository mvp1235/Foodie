package com.example.mvp.foodie.signup;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

public interface SignUpContract {
    interface View {
        void onSignUpSuccess(FirebaseUser user);
        void onSignUpFailure(String error);
    }

    interface Presenter {
        void signUp(Activity activity, String email, String password);
    }

    interface Interactor {
        void performFirebaseSignUp(Activity activity, String email, String password);
    }

    interface onRegistrationListener{
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(String message);
    }
}
