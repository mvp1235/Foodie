package com.example.mvp.foodie.intro;

import com.google.firebase.auth.FirebaseAuth;

public class IntroductionPresenter implements IntroductionContract.Presenter{
    IntroductionContract.View view;
    private FirebaseAuth mAuth;

    public IntroductionPresenter(IntroductionContract.View view) {
        this.view = view;
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void checkFirebaseAuth() {
        if (mAuth.getCurrentUser() != null)
            view.isLoggedIn(mAuth.getCurrentUser());
    }
}
