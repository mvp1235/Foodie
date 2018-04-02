package com.example.mvp.foodie.signin;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignInInteractor implements SignInContract.Interactor {
    private FirebaseAuth mAuth;
    private SignInContract.onSignInListener signInListener;

    public SignInInteractor(SignInContract.onSignInListener signInListener) {
        this.signInListener = signInListener;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void performFirebaseSignIn(Activity activity, String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        signInListener.onSuccess(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        String error = task.getException().toString();
                        signInListener.onFailure(error);

                    }
                }
            });
    }

}
