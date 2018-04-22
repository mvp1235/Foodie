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

    public EditProfilePresenter(ProfileContract.EditView view) {
        this.view = view;
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
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
                    updateEmailAndPassword(user, currentUser, currentEmail, newEmail, newPassword);
                } else {
                    view.onEditFailure("There is currently no users logged in.");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onEditFailure(databaseError.getMessage());
            }
        });
    }

    private void updateEmailAndPassword(final User user, final FirebaseUser currentUser, String currentEmail, final String newEmail, final String newPassword) {
        //update email if new email is in valid format and different than current email
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()
                && !currentEmail.equals(newEmail)) {

            currentUser.updateEmail(newEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.setEmail(newEmail);
                                //After successfully changed user auth email, update it accordingly in the database
                                userRef.child(user.getuID()).setValue(user);

                                //Updated email, next update password if requested
                                if (!TextUtils.isEmpty(newPassword)) {
                                    currentUser.updatePassword(newPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        view.onEditSuccess("Email address and password has been updated successfully.", user);
                                                    } else {
                                                        view.onEditSuccess("Email address has been updated successfully.", user);
                                                    }
                                                }
                                            });
                                } else {    //Password change was not requested
                                    view.onEditSuccess("Email address has been updated successfully.", user);
                                }
                            } else {
                                view.onEditFailure(task.getException().toString());
                            }
                        }
                    });

        } else {    //Email has not been changed
            //Update password if requested
            updatePasswordOnly(user, currentUser, newPassword);
        }
    }

    private void updatePasswordOnly(final User user, FirebaseUser currentUser, String newPassword) {
        //Update password
        if (!TextUtils.isEmpty(newPassword)) {
            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                view.onEditSuccess("Password has been updated successfully", user);
                            } else {
                                view.onEditFailure(task.getException().toString());
                            }
                        }
                    });
        } else {
            //If email and password are both not requested for changes
            userRef.child(user.getuID()).setValue(user);
            view.onEditSuccess("User profile has been updated successfully.", user);
        }
    }

}
