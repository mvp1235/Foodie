package com.example.mvp.foodie.profile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View view;
    private StorageReference storageReference;
    private DatabaseReference userReference;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
        storageReference = FirebaseStorage.getInstance().getReference();
        userReference = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public void loadDataFromFirebase(String userID) {
        obtainProfileURL(userID);
        obtainData(userID);
    }


    @Override
    public void uploadCapturedPhoto(BaseActivity activity, Bitmap profileBitmap, final String userID) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), profileBitmap, "Title", null);

        final Uri profileURI = Uri.parse(path);

        StorageReference filepath = storageReference.child("profilePhotos").child(userID);
        filepath.putFile(profileURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                obtainProfileURL(userID);
                view.onPhotoUploadSuccess(profileURI);
            }
        });

    }

    @Override
    public void uploadGalleryPhotoTo(BaseActivity activity, final Uri profileURI, final String userID) {
        StorageReference filepath = storageReference.child("profilePhotos").child(userID);
        filepath.putFile(profileURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                obtainProfileURL(userID);
                view.onPhotoUploadSuccess(profileURI);
            }
        });
    }

    private void obtainProfileURL(final String userID) {
        storageReference.child("profilePhotos").child(userID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                updateUserProfileURL(userID, uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void updateUserProfileURL(final String userID, final Uri uri) {
        userReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    user.setProfileURL(uri.toString());
                    userReference.child("Users").child(userID).setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void obtainData(String userID) {
        userReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                view.onLoadDataSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.onLoadDataFailure(databaseError.getMessage());
            }
        });
    }

}
