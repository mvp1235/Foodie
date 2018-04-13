package com.example.mvp.foodie.post;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.comment.PostCommentsActivity;
import com.example.mvp.foodie.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{

    private Context context;
    private List<Post> posts;

    public PostRecyclerAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_layout, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        final Post post = posts.get(position);

        holder.name.setText(post.getUser().getFullName());
        Picasso.get().load(post.getUser().getProfileURL()).into(holder.userProfile);
        holder.location.setText(post.getLocation());
        holder.time.setText(post.getPostDuration());
        holder.description.setText(post.getDescription());
        Picasso.get().load(post.getPhotoURL()).into(holder.postPhoto);
        holder.numComments.setText(post.getCommentCount());
        holder.numGoing.setText(post.getGoingCount());
        holder.numInterests.setText(post.getInterestCount());
        userLikedPost(holder.postHeart, post.getPostID(), (((BaseActivity)context).getmAuth().getCurrentUser().getUid()));


        holder.interestsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //increment or decrement interests count here, as well as toggle heart icons
            holder.postHeart.setImageResource(R.drawable.heart_filled);
            handleInterestClicked(holder, post.getPostID(), (((BaseActivity)context).getmAuth().getCurrentUser().getUid()));
            }
        });

        holder.commentsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Show all comments available for the specific post here
            Intent intent = new Intent(context, PostCommentsActivity.class);
            intent.putExtra("POST_ID", post.getPostID());
            context.startActivity(intent);
            }
        });

        holder.numGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //increment or decrement interests number for the specific post here

            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void userLikedPost(final AppCompatImageView heart, final String postID, final String userID) {
        final DatabaseReference databaseReference = ((BaseActivity)context).getmDatabase();
        databaseReference.child("Posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if(!post.getInterestIDs().contains(userID)) {
                    heart.setImageResource(R.drawable.heart_unfilled);
                } else {
                    heart.setImageResource(R.drawable.heart_filled);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleInterestClicked(final PostViewHolder holder, final String postID, final String userID) {
        final DatabaseReference databaseReference = ((BaseActivity)context).getmDatabase();

        databaseReference.child("Posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if(!post.getInterestIDs().contains(userID)) {
                    post.addInterestID(userID);
                    holder.postHeart.setImageResource(R.drawable.heart_filled);

                } else {
                    post.removeInterestID(userID);
                    holder.postHeart.setImageResource(R.drawable.heart_unfilled);
                }
                databaseReference.child("Posts").child(postID).setValue(post);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}