package com.example.mvp.foodie;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mvp.foodie.comment.PostCommentsActivity;
import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.example.mvp.foodie.post.EditPostActivity;
import com.example.mvp.foodie.post.PostViewHolder;
import com.example.mvp.foodie.profile.ProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mvp.foodie.UtilHelper.NUM_COMMENTS;
import static com.example.mvp.foodie.UtilHelper.NUM_INTERESTS;
import static com.example.mvp.foodie.UtilHelper.POST_DESCRIPTION;
import static com.example.mvp.foodie.UtilHelper.POST_ID;
import static com.example.mvp.foodie.UtilHelper.POST_LOCATION;
import static com.example.mvp.foodie.UtilHelper.POST_URL;
import static com.example.mvp.foodie.UtilHelper.REQUEST_CODE;
import static com.example.mvp.foodie.UtilHelper.REQUEST_DELETE_POST;
import static com.example.mvp.foodie.UtilHelper.REQUEST_EDIT_POST;
import static com.example.mvp.foodie.UtilHelper.USER_ID;
import static com.example.mvp.foodie.UtilHelper.VIEW_OTHER_PROFILE;

public class DetailPostActivity extends BaseActivity {

    private android.support.v7.widget.AppCompatTextView name, location, time, description, numInterests, numComments;
    private android.support.v7.widget.AppCompatImageView postPhoto, postHeart;
    private AppCompatImageButton menuBtn;
    private CircleImageView userProfile;
    private LinearLayout interestsLL, commentsLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        Intent intent = getIntent();

        final Post post = new Post();
        post.setPostID(intent.getStringExtra(POST_ID));
        post.setUserID(intent.getStringExtra(USER_ID));
        post.setDescription(intent.getStringExtra(POST_DESCRIPTION));
        post.setPhotoURL(intent.getStringExtra(POST_URL));
        post.setLocation(intent.getStringExtra(POST_LOCATION));

        String interestCount = intent.getStringExtra(NUM_INTERESTS);
        String commentCount = intent.getStringExtra(NUM_COMMENTS);

        initViews();

        setUserInfo(post.getUserID());
        location.setText(post.getLocation());
        time.setText(post.getPostDuration());
        description.setText(post.getDescription());
        Picasso.get().load(post.getPhotoURL()).into(postPhoto);
        numComments.setText(commentCount);
        numInterests.setText(interestCount);
        userLikedPost(post.getPostID(), getmAuth().getCurrentUser().getUid());


        postHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //increment or decrement interests count here, as well as toggle heart icons
                handleInterestClicked(post.getPostID(), post.getUserID(), getmAuth().getCurrentUser().getUid());
            }
        });

        numInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        commentsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show all comments available for the specific post here
                Intent intent = new Intent(DetailPostActivity.this, PostCommentsActivity.class);
                intent.putExtra(POST_ID, post.getPostID());
                startActivity(intent);
            }
        });


        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewUserProfile(post.getUserID());
            }
        });

        String postOwnerID = post.getUserID();
        String currentUserID = getmAuth().getCurrentUser().getUid();

        if (postOwnerID.equals(currentUserID)) {
            menuBtn.setVisibility(View.VISIBLE);
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPostPopupMenu(post);
                }
            });
        } else {
            menuBtn.setVisibility(View.GONE);
        }

    }

    private void initViews() {
        userProfile = findViewById(R.id.userProfilePhoto_id);
        name = findViewById(R.id.userName_id);
        location = findViewById(R.id.postLocation_id);
        time = findViewById(R.id.postTime_id);
        description = findViewById(R.id.postDescription_id);
        menuBtn = findViewById(R.id.postMenu_id);

        numInterests = findViewById(R.id.postNumInterest_id);
        numComments = findViewById(R.id.postNumComments_id);

        postPhoto = findViewById(R.id.postPhoto_id);
        postHeart = findViewById(R.id.postInterests_id);
        interestsLL = findViewById(R.id.interestsSection);
        commentsLL = findViewById(R.id.commentsSection_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_POST && resultCode == RESULT_OK) {
            finish();
        }
    }

    private void setUserInfo(String userID) {
        DatabaseReference userRef = getmDatabase().child("Users");
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                name.setText(u.getFullName());
                Picasso.get().load(u.getProfileURL()).into(userProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void viewUserProfile(String userID) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(REQUEST_CODE, VIEW_OTHER_PROFILE);
        intent.putExtra(USER_ID, userID);
        startActivityForResult(intent, VIEW_OTHER_PROFILE);
    }

    private void showPostPopupMenu(final Post post) {
        PopupMenu popupMenu = new PopupMenu(this, menuBtn);
        popupMenu.getMenuInflater().inflate(R.menu.post_option_menu_items, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase("Edit")) {
                    Intent intent = new Intent(DetailPostActivity.this, EditPostActivity.class);
                    intent.putExtra(POST_ID, post.getPostID());
                    intent.putExtra(USER_ID, post.getUserID());
                    intent.putExtra(POST_DESCRIPTION, post.getDescription());
                    intent.putExtra(POST_URL, post.getPhotoURL());
                    intent.putExtra(POST_LOCATION, post.getLocation());
                    startActivityForResult(intent, REQUEST_EDIT_POST);

                } else {
                    showDeleteConfirmationDialog(post);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showDeleteConfirmationDialog(final Post post) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this post?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost(post.getPostID(), post.getUserID());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deletePost(final String postID, final String userID) {
        final DatabaseReference postRef = getmDatabase().child("Posts");
        final DatabaseReference userRef = getmDatabase().child("Users");
        final DatabaseReference commentRef = getmDatabase().child("Comments");

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

                Toast.makeText(getApplicationContext(), "Post deleted...", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void userLikedPost( final String postID, final String userID) {
        final DatabaseReference databaseReference = getmDatabase();
        databaseReference.child("Posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if(!post.getInterestIDs().contains(userID)) {
                    postHeart.setImageResource(R.drawable.heart_unfilled);
                } else {
                    postHeart.setImageResource(R.drawable.heart_filled);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleInterestClicked(final String postID, final String ownerID, final String userID) {
        final DatabaseReference databaseReference = getmDatabase();

        databaseReference.child("Posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if(!post.getInterestIDs().contains(userID)) {
                    post.addInterestID(userID);
                    postHeart.setImageResource(R.drawable.heart_filled);

                    //If post owner like his/her own post, no need to post notification
                    if (!ownerID.equals(userID))
                        addNewPostLikeNotification(postID, userID);

                } else {
                    post.removeInterestID(userID);
                    postHeart.setImageResource(R.drawable.heart_unfilled);

                    //If post owner unlike his/her own post, no need to remove notification
                    if (!ownerID.equals(userID))
                        removeLikeNotification(postID, ownerID, userID);
                }
                databaseReference.child("Posts").child(postID).setValue(post);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewPostLikeNotification(final String postID, final String userID) {
        final DatabaseReference userRef = getmDatabase().child("Users");
        final DatabaseReference postRef = getmDatabase().child("Posts");
        final DatabaseReference notificationRef = getmDatabase().child("Notifications");

        final String newNotificationID = notificationRef.push().getKey();

        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Post currentPost = dataSnapshot.getValue(Post.class);

                userRef.child(currentPost.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final User postOwner = dataSnapshot.getValue(User.class);

                        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User likedUser = dataSnapshot.getValue(User.class);

                                Notification notification = new Notification();
                                notification.setnID(newNotificationID);
                                notification.setType("like");
                                notification.setContent("liked your post.");
                                notification.setUserName(likedUser.getFullName());
                                notification.setPhotoURL(likedUser.getProfileURL());
                                notification.setPostID(postID);
                                notification.setFromUserID(userID);
                                notification.setToUserID(postOwner.getuID());

                                notificationRef.child(currentPost.getUserID()).child(newNotificationID).setValue(notification);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeLikeNotification(final String postID, final String ownerID, final String userID) {
        final DatabaseReference notificationRef = getmDatabase().child("Notifications");

        notificationRef.child(ownerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Notification n = snapshot.getValue(Notification.class);
                    if (n != null && n.getContent().equals("liked your post.")
                            && n.getFromUserID().equals(userID) && n.getPostID().equals(postID)
                            && n.getToUserID().equals(ownerID)) {
                        notificationRef.child(ownerID).child(snapshot.getKey()).removeValue();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
