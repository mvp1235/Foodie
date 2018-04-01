package com.example.mvp.foodie.signup;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;

public class SignUpPresenter implements SignUpContract.Presenter, SignUpContract.onRegistrationListener {
    private SignUpContract.View signUpView;
    private SignUpInteractor signUpInteractor;

    public SignUpPresenter(SignUpContract.View signUpView) {
        this.signUpView = signUpView;
        signUpInteractor = new SignUpInteractor(this);
    }

    @Override
    public void signUp(Activity activity, String email, String password) {
        signUpInteractor.performFirebaseSignUp(activity, email, password);
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        signUpView.onSignUpSuccess(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        signUpView.onSignUpFailure(message);
    }
}
