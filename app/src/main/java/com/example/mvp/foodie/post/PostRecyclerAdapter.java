package com.example.mvp.foodie.post;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.comment.PostCommentsActivity;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.mvp.foodie.UtilHelper.POST_DESCRIPTION;
import static com.example.mvp.foodie.UtilHelper.POST_ID;
import static com.example.mvp.foodie.UtilHelper.POST_LOCATION;
import static com.example.mvp.foodie.UtilHelper.POST_URL;
import static com.example.mvp.foodie.UtilHelper.REQUEST_EDIT_POST;
import static com.example.mvp.foodie.UtilHelper.USER_ID;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder>{

    private Context context;
    private List<Post> posts;

    public PostRecyclerAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        this.notifyDataSetChanged();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_layout, parent, false);

        return new PostViewHolder(view);
    }

    private void setUserInfo(String userID, final PostViewHolder holder) {
        DatabaseReference userRef = ((BaseActivity)context).getmDatabase().child("Users");
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                holder.name.setText(u.getFullName());
                Picasso.get().load(u.getProfileURL()).into(holder.userProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        final Post post = posts.get(position);

        setUserInfo(post.getUserID(), holder);
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
            intent.putExtra(POST_ID, post.getPostID());
            context.startActivity(intent);
            }
        });

        holder.numGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //increment or decrement interests number for the specific post here

            }
        });

        String postOwnerID = post.getUserID();
        String currentUserID = ((BaseActivity)context).getmAuth().getCurrentUser().getUid();

        if (postOwnerID.equals(currentUserID)) {
            holder.menuBtn.setVisibility(View.VISIBLE);
            holder.menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                showPostPopupMenu(post, holder);
                }
            });
        } else {
            holder.menuBtn.setVisibility(View.GONE);
        }



    }

    private void showPostPopupMenu(final Post post, PostViewHolder holder) {
        PopupMenu popupMenu = new PopupMenu(context, holder.menuBtn);
        popupMenu.getMenuInflater().inflate(R.menu.post_option_menu_items, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase("Edit")) {
                    Intent intent = new Intent(context, EditPostActivity.class);
                    intent.putExtra(POST_ID, post.getPostID());
                    intent.putExtra(USER_ID, post.getUserID());
                    intent.putExtra(POST_DESCRIPTION, post.getDescription());
                    intent.putExtra(POST_URL, post.getPhotoURL());
                    intent.putExtra(POST_LOCATION, post.getLocation());
                    ((BaseActivity) context).startActivityForResult(intent, REQUEST_EDIT_POST);

                } else {
                    deletePost(post.getPostID(), post.getUserID());
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    private void deletePost(final String postID, final String userID) {
        final DatabaseReference postRef = ((BaseActivity)context).getmDatabase().child("Posts");
        final DatabaseReference userRef = ((BaseActivity)context).getmDatabase().child("Users");
        final DatabaseReference commentRef = ((BaseActivity)context).getmDatabase().child("Comments");

        //Delete Post
        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);

                List<String> commentIDs = post.getCommentIDs();

                //Delete comments from post
                for (final String id : commentIDs) {
                    commentRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            commentRef.child(id).removeValue();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                //Delete postIDs in user
                userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        u.deletePostID(postID);
                        userRef.child(userID).setValue(u);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Delete post
                postRef.child(postID).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
