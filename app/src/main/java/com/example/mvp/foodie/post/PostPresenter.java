package com.example.mvp.foodie.post;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mvp.foodie.UtilHelper.INITIAL_QUERY;

public class PostPresenter implements PostContract.Presenter {

    private PostContract.View view;
    private PostContract.EditView editView;
    private PostContract.DetailView detailView;
    private PostContract.ListView listView;
    private StorageReference storageReference;


    public PostPresenter(PostContract.View view) {
        this.view = view;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public PostPresenter(PostContract.EditView editView) {
        this.editView = editView;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public PostPresenter(PostContract.DetailView detailView) {
        this.detailView = detailView;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public PostPresenter(PostContract.ListView listView) {
        this.listView = listView;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void getLocation(BaseActivity activity, String providedLocation) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(activity);
            intent.putExtra(INITIAL_QUERY, providedLocation);
            if (view != null)
                view.onLocationPickedSuccess(intent);
            if (editView != null)
                editView.onLocationPickedSuccess(intent);

        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
            if (view != null)
                view.onLocationPickedFailure(e.getMessage());
            if (editView != null)
                editView.onLocationPickedFailure(e.getMessage());

        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            if (view != null)
                view.onLocationPickedFailure(e.getMessage());
            if (editView != null)
                editView.onLocationPickedFailure(e.getMessage());
        }
    }

    @Override
    public void uploadPost(BaseActivity activity, ImageView imageView, final String description, final String location, final String userID) {
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final DatabaseReference postRef = activity.getmDatabase().child("Posts");
        final DatabaseReference userRef = activity.getmDatabase().child("Users");
        final String newPostID = postRef.push().getKey();

        StorageReference postPhotosRef = storageReference.child("postPhotos").child(newPostID);
        UploadTask uploadTask = postPhotosRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                view.onPostFailed(exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                final Post post = new Post();
                post.setDescription(description);
                post.setLocation(location);
                post.setPhotoURL(downloadUrl.toString());
                post.setPostID(newPostID);
                post.addSubscriberID(userID);

                userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);

                        //Update user database
                        u.addPostID(newPostID);
                        userRef.child(u.getuID()).setValue(u);

                        //Update post database
                        post.setUserID(u.getuID());
                        postRef.child(newPostID).setValue(post);
                        view.onPostCreated(post);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view.onPostFailed(databaseError.getMessage());
                    }
                });


            }
        });
    }

    @Override
    public void editPost(final BaseActivity activity, ImageView imageView, final String description, final String location, String userID, final String postID) {
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();

        final DatabaseReference databaseReference = activity.getmDatabase().child("Posts");
//        final String newPostID = databaseReference.push().getKey();

        StorageReference postPhotosRef = storageReference.child("postPhotos").child(postID);
        UploadTask uploadTask = postPhotosRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                editView.onPostEditFailure(exception.toString());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                activity.getmDatabase().child("Posts").child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Post post = dataSnapshot.getValue(Post.class);
                        post.setDescription(description);
                        post.setLocation(location);
                        post.setPhotoURL(downloadUrl.toString());
                        post.setCreatedTime(System.currentTimeMillis());  //update createdTime

                        databaseReference.child(postID).setValue(post);

                        editView.onPostEditSuccess(post);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        editView.onPostEditFailure(databaseError.getMessage());
                    }
                });

            }
        });
    }

    @Override
    public void deletePost(BaseActivity activity, final String postID, final String ownerID) {
        final DatabaseReference postRef = activity.getmDatabase().child("Posts");
        final DatabaseReference userRef = activity.getmDatabase().child("Users");
        final DatabaseReference commentRef = activity.getmDatabase().child("Comments");

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
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }

                //Delete postIDs in user
                userRef.child(ownerID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        u.deletePostID(postID);
                        userRef.child(ownerID).setValue(u);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                //Delete post
                postRef.child(postID).removeValue();

                if (detailView != null)
                    detailView.onPostDeleteSuccess("Post deleted successfully.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (detailView != null)
                    detailView.onPostDeleteFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void checkIfUserLikedPost(BaseActivity activity, String postID, final String userID) {
        final DatabaseReference postRef = activity.getmDatabase().child("Posts");
        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if(!post.getInterestIDs().contains(userID)) {
                    detailView.onUserNotLikedPost();
                } else {
                    detailView.onUserLikedPost();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void loadDetailPost(BaseActivity activity, String postID, String postOwnerID, String currentUserID) {
        DatabaseReference postRef = activity.getmDatabase().child("Posts");
        final DatabaseReference userRef = activity.getmDatabase().child("Users");

        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Post post = dataSnapshot.getValue(Post.class);

                String postOwnerID = post.getUserID();
                userRef.child(postOwnerID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User postOwner = dataSnapshot.getValue(User.class);
                        detailView.onLoadPostSuccess(post, postOwner);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        detailView.onLoadPostFailure(databaseError.getMessage());
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                detailView.onLoadPostFailure(databaseError.getMessage());
            }
        });
    }


    @Override
    public void handleUserInterestClick(final BaseActivity activity, String postID, final String userID) {
        final DatabaseReference postRef = activity.getmDatabase().child("Posts");

        postRef.child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if(!post.getInterestIDs().contains(userID)) {
                    post.addInterestID(userID);

                    detailView.onUserLikedPost();

                    //If post owner like his/her own post, no need to post notification
                    if (!post.getUserID().equals(userID))
                        addNewPostLikeNotification(activity, post.getPostID(), userID);


                    detailView.incrementInterestCount(post.getInterestCount());

                } else {
                    post.removeInterestID(userID);
                    detailView.onUserNotLikedPost();

                    //If post owner unlike his/her own post, no need to remove notification
                    if (!post.getUserID().equals(userID))
                        removeLikeNotification(activity, post.getPostID(), post.getUserID(), userID);

                    detailView.decrementInterestCount(post.getInterestCount());
                }
                postRef.child(post.getPostID()).setValue(post);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                detailView.displayInterestCountChangeError(databaseError.getMessage());
            }
        });
    }

    private void addNewPostLikeNotification(BaseActivity activity, final String postID, final String userID) {
        final DatabaseReference userRef = activity.getmDatabase().child("Users");
        final DatabaseReference postRef = activity.getmDatabase().child("Posts");
        final DatabaseReference notificationRef = activity.getmDatabase().child("Notifications");

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

    private void removeLikeNotification(BaseActivity activity, final String postID, final String ownerID, final String userID) {
        final DatabaseReference notificationRef = activity.getmDatabase().child("Notifications");

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

    @Override
    public void loadAllUserPosts(BaseActivity activity, String userID) {
        final DatabaseReference postRef = activity.getmDatabase().child("Posts");
        DatabaseReference userRef = activity.getmDatabase().child("Users");

        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                List<String> postIDs = currentUser.getPostIDs();

                for (String id : postIDs) {
                    postRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Post post = dataSnapshot.getValue(Post.class);
                            listView.onLoadPostSuccess(post);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listView.onLoadPostFailure(databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listView.onLoadPostFailure(databaseError.getMessage());
            }
        });
    }
}
