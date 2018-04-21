package com.example.mvp.foodie.signup;

import android.support.annotation.NonNull;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class SignUpInteractor implements SignUpContract.Interactor {

    private SignUpContract.onSignUpListener signUpListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public SignUpInteractor(SignUpContract.onSignUpListener listener) {
        this.signUpListener = listener;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void performFirebaseSignUp(final BaseActivity activity, final String firstName, final String lastName, final String email, final String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            signUpListener.onFailure(task.getException().getMessage());
                        } else {
                            performUserDataStoring(activity, firstName, lastName, email);
                            signUpListener.onSuccess(task.getResult().getUser());
                        }
                    }
                });
    }

    @Override
    public void performUserDataStoring(BaseActivity activity, String firstName, String lastName, String emailAddress) {
        User user = new User();
        user.setuID(mAuth.getCurrentUser().getUid());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(emailAddress);
        user.addTokenID(FirebaseInstanceId.getInstance().getToken());

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);
    }

}
