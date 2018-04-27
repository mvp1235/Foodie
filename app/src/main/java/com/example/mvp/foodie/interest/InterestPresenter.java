package com.example.mvp.foodie.interest;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Interest;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class InterestPresenter implements InterestContract.Presenter {
    private InterestContract.View view;

    public InterestPresenter(InterestContract.View view) {
        this.view = view;
    }

    @Override
    public void loadInterests(BaseActivity activity, String postID) {
        final DatabaseReference postRef = activity.getmDatabase().child("Posts");
        final DatabaseReference userRef = activity.getmDatabase().child("Users");

        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                List<String> interestList = post.getInterestIDs();

                for (String userID : interestList) {
                    userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            Interest interest = new Interest();
                            interest.setProfileURL(user.getProfileURL());
                            interest.setUserName(user.getFullName());
                            view.onLoadInterestsSuccess(interest);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            view.onLoadInterestFailure(databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onLoadInterestFailure(databaseError.getMessage());
            }
        });

    }
}
