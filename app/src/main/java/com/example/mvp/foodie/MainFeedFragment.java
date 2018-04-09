package com.example.mvp.foodie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mvp.foodie.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainFeedFragment extends Fragment {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<Post> posts;


    public MainFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_feed, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_id);

        posts = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadAllPosts(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        return view;
    }

    private void loadAllPosts(DataSnapshot dataSnapshot) {
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Post p = singleSnapshot.getValue(Post.class);
            posts.add(p);
//            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, allTask);
//            recyclerView.setAdapter(recyclerViewAdapter);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapter(getContext(), posts));

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.addItemDecoration(mDividerItemDecoration);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
