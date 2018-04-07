package com.example.mvp.foodie.signin;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInPresenter implements SignInContract.Presenter, SignInContract.onSignInListener {
    private SignInContract.Interactor signInInteractor;
    private SignInContract.View signinView;
    private FirebaseAuth mAuth;

    public SignInPresenter(SignInContract.View signinView) {
        this.signinView = signinView;
        signInInteractor = new SignInInteractor(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(Activity activity, String email, String password) {
        signInInteractor.performFirebaseSignIn(activity, email, password);
    }

    @Override
    public void checkFirebaseAuth() {
        if (mAuth.getCurrentUser() != null)
            signinView.isLoggedIn(mAuth.getCurrentUser());
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        signinView.onSignInSuccess(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        signinView.onSignInFailure(message);
    }
}
