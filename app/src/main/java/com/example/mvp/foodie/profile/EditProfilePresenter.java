package com.example.mvp.foodie.profile;

import android.app.Activity;
import android.content.Intent;

import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.mvp.foodie.UtilHelper.EMAIL;
import static com.example.mvp.foodie.UtilHelper.FULL_NAME;


public class EditProfilePresenter implements ProfileContract.EditPresenter {
    private ProfileContract.EditView view;
    private DatabaseReference userReference;

    public EditProfilePresenter(ProfileContract.EditView view) {
        this.view = view;
        userReference = FirebaseDatabase.getInstance().getReference();
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
    public void edit(Activity activity, final String userID, final String firstName, final String lastName, final String email) {
        userReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    userReference.child("Users").child(userID).setValue(user);
                    view.onEditSuccess(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onEditFailure(databaseError.getMessage());
            }
        });
    }
}
