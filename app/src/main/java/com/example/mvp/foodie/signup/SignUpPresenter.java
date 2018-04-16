package com.example.mvp.foodie.signup;

import com.example.mvp.foodie.BaseActivity;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPresenter implements SignUpContract.Presenter, SignUpContract.onSignUpListener{
    private SignUpContract.View signUpView;
    private SignUpContract.Interactor interactor;

    public SignUpPresenter(SignUpContract.View signUpView) {
        this.signUpView = signUpView;
        interactor = new SignUpInteractor(this);
    }

    @Override
    public void signUp(BaseActivity activity, String firstName, String lastName, String email, String password) {
        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();
        interactor.performFirebaseSignUp(activity, firstName, lastName, email, password);
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
