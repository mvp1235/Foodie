package com.example.mvp.foodie.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.comment.PostCommentsActivity;
import com.example.mvp.foodie.interest.InterestListActivity;
import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.example.mvp.foodie.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
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
import static com.example.mvp.foodie.UtilHelper.REQUEST_EDIT_POST;
import static com.example.mvp.foodie.UtilHelper.USER_ID;
import static com.example.mvp.foodie.UtilHelper.VIEW_OTHER_PROFILE;

public class DetailPostActivity extends BaseActivity implements PostContract.DetailView {

    private android.support.v7.widget.AppCompatTextView name, location, time, description, numInterests, numComments;
    private android.support.v7.widget.AppCompatImageView postPhoto, postHeart;
    private AppCompatImageButton menuBtn;
    private CircleImageView userProfile;
    private LinearLayout commentsLL;

    private PostContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        Intent intent = getIntent();
        final String postID = intent.getStringExtra(POST_ID);
        final String postOwnerID = intent.getStringExtra(USER_ID);

        initViews();

        presenter.loadDetailPost(this, postID, postOwnerID, FirebaseAuth.getInstance().getCurrentUser().getUid());
        presenter.checkIfUserLikedPost(this, postID, FirebaseAuth.getInstance().getCurrentUser().getUid());


        postHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //increment or decrement interests count here, as well as toggle heart icons
                presenter.handleUserInterestClick(DetailPostActivity.this, postID, FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

        numInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //view list of all users who liked the post
                Intent intent = new Intent(DetailPostActivity.this, InterestListActivity.class);
                intent.putExtra(POST_ID, postID);
                startActivity(intent);

            }
        });

        commentsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show all comments available for the specific post here
                Intent intent = new Intent(DetailPostActivity.this, PostCommentsActivity.class);
                intent.putExtra(POST_ID, postID);
                startActivity(intent);
            }
        });


        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewUserProfile(postOwnerID);
            }
        });

        String currentUserID = getmAuth().getCurrentUser().getUid();

        if (postOwnerID.equals(currentUserID)) {
            menuBtn.setVisibility(View.VISIBLE);
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPostPopupMenu(postID, postOwnerID);
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
        commentsLL = findViewById(R.id.commentsSection_id);

        presenter = new PostPresenter(this);
    }


    private void viewUserProfile(String userID) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(REQUEST_CODE, VIEW_OTHER_PROFILE);
        intent.putExtra(USER_ID, userID);
        startActivityForResult(intent, VIEW_OTHER_PROFILE);
    }

    private void showPostPopupMenu(final String postID, final String postOwnerID) {
        PopupMenu popupMenu = new PopupMenu(this, menuBtn);
        popupMenu.getMenuInflater().inflate(R.menu.post_option_menu_items, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase("Edit")) {
                    final Intent intent = new Intent(DetailPostActivity.this, EditPostActivity.class);

                    getmDatabase().child("Posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Post post = dataSnapshot.getValue(Post.class);
                            intent.putExtra(POST_ID, post.getPostID());
                            intent.putExtra(USER_ID, post.getUserID());
                            intent.putExtra(POST_DESCRIPTION, post.getDescription());
                            intent.putExtra(POST_URL, post.getPhotoURL());
                            intent.putExtra(POST_LOCATION, post.getLocation());
                            startActivityForResult(intent, REQUEST_EDIT_POST);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    showDeleteConfirmationDialog(postID, postOwnerID);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showDeleteConfirmationDialog(final String postID, final String postOwnerID) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this post?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deletePost(DetailPostActivity.this, postID, postOwnerID);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_POST && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void onPostDeleteSuccess(String successMessage) {
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPostDeleteFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void incrementInterestCount(String incrementedCount) {
        numInterests.setText(incrementedCount);
    }

    @Override
    public void decrementInterestCount(String decrementedCount) {
        numInterests.setText(decrementedCount);
    }

    @Override
    public void displayInterestCountChangeError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadPostSuccess(final Post post, final User postOwner) {
        location.setText(post.getLocation());
        time.setText(post.getPostDuration());
        description.setText(post.getDescription());
        numComments.setText(post.getCommentCount());
        numInterests.setText(post.getInterestCount());

        name.setText(postOwner.getFullName());

        Picasso.get().load(post.getPhotoURL())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(postPhoto, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(post.getPhotoURL()).into(postPhoto);
                    }
                });

        Picasso.get().load(postOwner.getProfileURL())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(userProfile, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(postOwner.getProfileURL()).into(userProfile);
                    }
                });
    }

    @Override
    public void onLoadPostFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserLikedPost() {
        postHeart.setImageResource(R.drawable.heart_filled);
    }

    @Override
    public void onUserNotLikedPost() {
        postHeart.setImageResource(R.drawable.heart_unfilled);
    }
}
