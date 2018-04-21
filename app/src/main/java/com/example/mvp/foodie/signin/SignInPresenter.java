package com.example.mvp.foodie.signin;

import android.support.annotation.NonNull;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class SignInPresenter implements SignInContract.Presenter {
    private SignInContract.View signinView;
    private FirebaseAuth mAuth;

    public SignInPresenter(SignInContract.View signinView) {
        this.signinView = signinView;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(final BaseActivity activity, String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = mAuth.getCurrentUser();

                            final String tokenID = FirebaseInstanceId.getInstance().getToken();

                            final DatabaseReference userRef = activity.getmDatabase().child("Users");

                            userRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User u = dataSnapshot.getValue(User.class);
                                    u.addTokenID(tokenID);

                                    userRef.child(user.getUid()).setValue(u);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            signinView.onSignInSuccess(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            String error = task.getException().toString();
                            String message;
                            if (error.contains("There is no user record corresponding to this identifier"))
                                message = "There are no existing accounts with this email address.";
                            else if (error.contains("The password is invalid"))
                                message = "Invalid password.";
                            else
                                message = error;
                            signinView.onSignInFailure(message);

                        }
                    }
                });
    }

    @Override
    public void checkFirebaseAuth() {
        if (mAuth.getCurrentUser() != null)
            signinView.isLoggedIn(mAuth.getCurrentUser());
    }
}
