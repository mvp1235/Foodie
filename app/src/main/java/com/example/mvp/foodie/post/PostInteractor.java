package com.example.mvp.foodie.post;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
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

import java.io.ByteArrayOutputStream;

import static com.example.mvp.foodie.UtilHelper.INITIAL_QUERY;

public class PostInteractor implements PostContract.Interactor {

    private StorageReference storageReference;
    private PostContract.onPostCreateListener postListener;
    private PostContract.onLocationPickedListener locationListener;
    private PostContract.onPostEditListener editListener;

    public PostInteractor(PostContract.onPostCreateListener postListener, PostContract.onLocationPickedListener locationListener) {
        this.postListener = postListener;
        this.locationListener = locationListener;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public PostInteractor(PostContract.onPostEditListener editListener, PostContract.onLocationPickedListener locationListener) {
        this.editListener = editListener;
        this.locationListener = locationListener;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void performGetLocation(BaseActivity activity, String providedLocation) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(activity);
            intent.putExtra(INITIAL_QUERY, providedLocation);
            locationListener.onLocationPickedSuccess(intent);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
            locationListener.onLocationPickedFailure(e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
            locationListener.onLocationPickedFailure(e.getMessage());
        }
    }


    @Override
    public void uploadPostToFirebaseStorage(final BaseActivity activity, ImageView imageView, final String description, final String location, final String userID) {
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
                postListener.onPostFailure(exception.toString());
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
                        postListener.onPostSuccess(post);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        postListener.onPostFailure(databaseError.getMessage());
                    }
                });


            }
        });

    }

    @Override
    public void editPostOnFirebase(final BaseActivity activity, ImageView imageView, final String description, final String location, final String userID, final String postID) {
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final DatabaseReference databaseReference = activity.getmDatabase().child("Posts");
//        final String newPostID = databaseReference.push().getKey();

        StorageReference postPhotosRef = storageReference.child("postPhotos").child(postID);
        UploadTask uploadTask = postPhotosRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                editListener.onEditFailure(exception.toString());
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

                        databaseReference.child(postID).setValue(post);
                        editListener.onEditSuccess(post);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        editListener.onEditFailure(databaseError.getMessage());
                    }
                });

            }
        });
    }

    @Override
    public void deletePostOnFirebase(BaseActivity activity, String postID) {

    }

}
