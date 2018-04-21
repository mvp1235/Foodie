package com.example.mvp.foodie.signup;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.mvp.foodie.BaseActivity;
import com.google.firebase.auth.FirebaseUser;

public interface SignUpContract {
    interface View {
        void onSignUpSuccess(FirebaseUser user);
        void onSignUpFailure(String error);
    }

    interface Presenter {
        void signUp(BaseActivity activity, String firstName, String lastName, String email, String password);
    }

}
