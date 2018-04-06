package com.example.mvp.foodie.profile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.mvp.foodie.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ProfileInteractor implements ProfileContract.Interactor {
    private ProfileContract.onEditListener editListener;
    private ProfileContract.onUploadListener uploadListener;
    private ProfileContract.onLoadListener loadListener;
    private StorageReference storageReference;
    private DatabaseReference userReference;

    public ProfileInteractor(ProfileContract.onEditListener editListener, ProfileContract.onUploadListener uploadListener, ProfileContract.onLoadListener loadListener) {
        this.editListener = editListener;
        this.uploadListener = uploadListener;
        this.loadListener = loadListener;
        storageReference = FirebaseStorage.getInstance().getReference();
        userReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void uploadCapturedPhotoToFirebase(final Activity activity, final Bitmap profileBitmap, final String userID) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), profileBitmap, "Title", null);

        final Uri profileURI = Uri.parse(path);

        StorageReference filepath = storageReference.child("profilePhotos").child(userID);
        filepath.putFile(profileURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                obtainProfileURL(userID);
                uploadListener.onPhotoUploadSuccess(profileURI);
            }
        });
    }

    @Override
    public void uploadGalleryPhotoToFirebase(final Activity activity, final Uri profileURI, final String userID) {
        StorageReference filepath = storageReference.child("profilePhotos").child(userID);
        filepath.putFile(profileURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                obtainProfileURL(userID);
                uploadListener.onPhotoUploadSuccess(profileURI);
            }
        });
    }

    @Override
    public void performLoadData(String userID) {
        obtainProfileURL(userID);
        obtainData(userID);
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
                loadListener.onLoadDataSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadListener.onLoadDataFailure(databaseError.getMessage());
            }
        });
    }

    @Override
    public void performProfileEdit(Activity activity, final String userID, final String firstName, final String lastName, final String email) {
        userReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                userReference.child("Users").child(userID).setValue(user);

                editListener.onEditSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                editListener.onEditFailure(databaseError.getMessage());
            }
        });
    }
}
