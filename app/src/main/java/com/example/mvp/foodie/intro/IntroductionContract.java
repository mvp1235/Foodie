package com.example.mvp.foodie.intro;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

public interface IntroductionContract {
    interface View {
        void isLoggedIn(FirebaseUser user);
    }

    interface Presenter {
        void checkFirebaseAuth();

    }

}
