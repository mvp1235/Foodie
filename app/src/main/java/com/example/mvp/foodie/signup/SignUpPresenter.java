package com.example.mvp.foodie.signup;

import android.support.annotation.NonNull;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View signUpView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public SignUpPresenter(SignUpContract.View signUpView) {
        this.signUpView = signUpView;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signUp(final BaseActivity activity, String firstName, String lastName, final String email, String password) {
        firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1).toLowerCase();
        lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1).toLowerCase();

        final String first = firstName;
        final String last = lastName;

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            signUpView.onSignUpFailure(task.getException().toString());
                        } else {
                            performUserDataStoring(activity, first, last, email);
                            signUpView.onSignUpSuccess(task.getResult().getUser());
                        }
                    }
                });
    }

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
