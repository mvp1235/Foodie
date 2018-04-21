package com.example.mvp.foodie.navigation_drawer;

import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrawerPresenter implements DrawerContract.Presenter {
    private DrawerContract.View view;
    private DatabaseReference userReference;

    public DrawerPresenter(DrawerContract.View view) {
        this.view = view;
        userReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void loadData(String userID) {
        userReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                view.onLoadDataSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onLoadDataFailure(databaseError.getMessage());
            }
        });
    }

}
