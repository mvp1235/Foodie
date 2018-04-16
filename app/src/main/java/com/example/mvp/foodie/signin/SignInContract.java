package com.example.mvp.foodie.signin;

import com.example.mvp.foodie.BaseActivity;
import com.google.firebase.auth.FirebaseUser;

public interface SignInContract {
    interface View {
        void onSignInSuccess(FirebaseUser user);
        void onSignInFailure(String error);
        void isLoggedIn(FirebaseUser user);
    }

    interface Presenter {
        void signIn(BaseActivity activity, String email, String password);
        void checkFirebaseAuth();
    }

    interface Interactor {
        void performFirebaseSignIn(BaseActivity activity, String email, String password);
    }

    interface onSignInListener{
        void onSuccess(FirebaseUser firebaseUser);
        void onFailure(String message);
    }
}
