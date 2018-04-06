package com.example.mvp.foodie.navigation_drawer;

import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DrawerInteractor implements DrawerContract.Interactor {
    private DrawerContract.onLoadListener loadListener;
    private DatabaseReference userReference;

    public DrawerInteractor(DrawerContract.onLoadListener loadListener) {
        this.loadListener = loadListener;
        userReference = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    public void performLoadData(String userID) {
        userReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                loadListener.onLoadSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadListener.onLoadFailure(databaseError.getMessage());
            }
        });
    }
}
