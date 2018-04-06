package com.example.mvp.foodie.signup;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.mvp.foodie.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


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
    public void performFirebaseSignUp(final Activity activity, final String firstName, final String lastName, final String email, final String password) {
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
    public void performUserDataStoring(Activity activity, String firstName, String lastName, String emailAddress) {
        User user = new User(mAuth.getCurrentUser().getUid());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(emailAddress);

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);
    }

}
