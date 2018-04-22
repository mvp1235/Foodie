package com.example.mvp.foodie.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.mvp.foodie.UtilHelper.EMAIL;
import static com.example.mvp.foodie.UtilHelper.FULL_NAME;


public class EditProfilePresenter implements ProfileContract.EditPresenter {
    private ProfileContract.EditView view;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    public EditProfilePresenter(ProfileContract.EditView view) {
        this.view = view;
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void loadData(Intent intent) {
        if (intent == null)
            view.onLoadFailure("Cannot load data.");
        else {
            String fullName = intent.getStringExtra(FULL_NAME);
            String[] nameSplit = fullName.split(" ");
            String firstName = nameSplit[0];
            String lastName = nameSplit[1];
            String email = intent.getStringExtra(EMAIL);

            view.onLoadSuccess(firstName, lastName, email);
        }
    }

    @Override
    public void updateUserProfile(BaseActivity activity, final String firstName, final String lastName, final String currentEmail, final String newEmail, final String newPassword) {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                //Update first and last name
                user.setFirstName(firstName);
                user.setLastName(lastName);

                //Update email and password if requested
                if (currentUser != null) {
                    updateEmail(currentUser, currentEmail, newEmail);
                    updatePassword(currentUser, newPassword);
                } else {
                    view.onEditFailure("There is currently no users logged in.");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void updateEmail(FirebaseUser currentUser, String currentEmail, String newEmail) {
        //update email
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()
                && !currentEmail.equals(newEmail)) {

            currentUser.updateEmail(newEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                view.onEditFailure(task.getException().toString());
                            }
                        }
                    });

        }
    }

    private void updatePassword(FirebaseUser currentUser, String newPassword) {
        //Update password
        if (!TextUtils.isEmpty(newPassword)) {
            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                view.onEditFailure(task.getException().toString());
                            }
                        }
                    });
        }
    }


//    @Override
//    public void edit(BaseActivity activity, final String userID, final String firstName, final String lastName, final String email) {
//        userReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                if (user != null) {
//                    user.setFirstName(firstName);
//                    user.setLastName(lastName);
//                    user.setEmail(email);
//                    userReference.child("Users").child(userID).setValue(user);
//                    view.onEditSuccess(user);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                view.onEditFailure(databaseError.getMessage());
//            }
//        });
//    }
}
