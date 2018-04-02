package com.example.mvp.foodie.signup;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class SignUpInteractor implements SignUpContract.Interactor {

    private SignUpContract.onSignUpListener signUpListener;
    private SignUpContract.onUploadListener uploadListener;
    private StorageReference storageReference;

    public SignUpInteractor(SignUpContract.onSignUpListener listener, SignUpContract.onUploadListener uploadListener) {
        this.signUpListener = listener;
        this.uploadListener = uploadListener;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void performFirebaseSignUp(Activity activity, String firstName, String lastName, String email, String password) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            signUpListener.onFailure(task.getException().getMessage());
                        } else {
                            signUpListener.onSuccess(task.getResult().getUser());
                        }
                    }
                });
    }

    @Override
    public void uploadCapturedPhotoToFirebase(final Activity activity, Bitmap profileBitmap, String userID) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), profileBitmap, "Title", null);

        final Uri profileURI = Uri.parse(path);

        StorageReference filepath = storageReference.child("profilePhotos").child(userID);
        filepath.putFile(profileURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                uploadListener.onPhotoUploadSuccess(profileURI);
            }
        });
    }

    @Override
    public void uploadGalleryPhotoToFirebase(final Activity activity, final Uri profileURI, String userID) {

        StorageReference filepath = storageReference.child("profilePhotos").child(userID);
        filepath.putFile(profileURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                uploadListener.onPhotoUploadSuccess(profileURI);
            }
        });

    }
}
