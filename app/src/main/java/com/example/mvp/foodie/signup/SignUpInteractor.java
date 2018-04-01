package com.example.mvp.foodie.signup;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpInteractor implements SignUpContract.Interactor {
    private static final String TAG = SignUpInteractor.class.getSimpleName();
    private SignUpContract.onRegistrationListener listener;

    public SignUpInteractor(SignUpContract.onRegistrationListener listener) {
        this.listener = listener;
    }

    @Override
    public void performFirebaseSignUp(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onFailure(task.getException().getMessage());
                        } else {
                            listener.onSuccess(task.getResult().getUser());
                        }
                    }
                });
    }
}
