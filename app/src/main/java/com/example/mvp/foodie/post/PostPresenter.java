package com.example.mvp.foodie.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

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

public class PostPresenter implements PostContract.Presenter {

    private PostContract.View view;
    private PostContract.EditView editView;
    private StorageReference storageReference;


    public PostPresenter(PostContract.View view) {
        this.view = view;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public PostPresenter(PostContract.EditView editView) {
        this.editView = editView;
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
        byte[] data = baos.toByteArray();

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
    public void deletePost(BaseActivity activity, String postID) {

    }


}
