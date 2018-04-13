package com.example.mvp.foodie.main_feed;

import com.example.mvp.foodie.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainFeedInteractor implements MainFeedContract.Interactor {
    private MainFeedContract.onLoadListener loadListener;
    private DatabaseReference databaseReference;

    public MainFeedInteractor(MainFeedContract.onLoadListener loadListener) {
        this.loadListener = loadListener;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void loadPostsFromFirebase() {
        databaseReference.child("Posts").orderByChild("createdTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> posts = new ArrayList<>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Post p = singleSnapshot.getValue(Post.class);
                    posts.add(p);
                }

                Collections.reverse(posts);

                loadListener.onLoadSuccess(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadListener.onLoadFailure(databaseError.getMessage());
            }
        });
    }

}
