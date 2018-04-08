package com.example.mvp.foodie.post;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Post;
import com.example.mvp.foodie.models.User;
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

public class PostInteractor implements PostContract.Interactor {

    private StorageReference storageReference;
    private PostContract.onPostCreateListener listener;

    public PostInteractor(PostContract.onPostCreateListener listener) {
        this.listener = listener;
        storageReference = FirebaseStorage.getInstance().getReference();
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

        final DatabaseReference databaseReference = activity.getmDatabase().child("Posts");
        final String newPostID = databaseReference.push().getKey();

        StorageReference postPhotosRef = storageReference.child("postPhotos").child(newPostID);
        UploadTask uploadTask = postPhotosRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                listener.onPostFailure(exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();


                final Post post = new Post(newPostID);
                post.setDescription(description);
                post.setLocation(location);
                post.setPhotoURL(downloadUrl.toString());

                activity.getmDatabase().child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        post.setUser(u);
                        databaseReference.child(newPostID).setValue(post);
                        listener.onPostSuccess(post);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.onPostFailure(databaseError.getMessage());
                    }
                });


            }
        });

    }

}
