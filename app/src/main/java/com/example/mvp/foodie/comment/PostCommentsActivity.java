package com.example.mvp.foodie.comment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.User;

import java.util.ArrayList;

public class PostCommentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        recyclerView = findViewById(R.id.recyclerView_id);


        ArrayList<Comment> comments = new ArrayList<>();
        Comment c = new Comment();
        User u = new User();
        u.setProfileURL("http://www.dewaterpoort.nl/wp-content/uploads/2015/01/profiel.jpg");
        u.setFirstName("Huy");
        u.setLastName("Nguyen");
        c.setUser(u);
        c.setContent("Definitely going tomorrow!!");

        comments.add(c);
        comments.add(c);
        comments.add(c);
        comments.add(c);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CommentRecyclerAdapter(this, comments));

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());

        recyclerView.addItemDecoration(mDividerItemDecoration);
    }
}
