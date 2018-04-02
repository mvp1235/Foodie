package com.example.mvp.foodie.signup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mvp.foodie.MainActivity;
import com.example.mvp.foodie.R;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View, SignUpContract.onUploadListener {

    private static final int REQUEST_GALLERY_PHOTO = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 201;
    private static final int REQUEST_ALL = 202;
    private static final int REQUEST_WRITE_EXTERNAL = 203;

    AppCompatImageView profilePhotoIV;
    AppCompatEditText firstNameET, lastNameET, emailET, passwordET;
    AppCompatButton signUpBtn;

    ProgressDialog mPrgressDialog;
    private SignUpPresenter presenter;

    private AlertDialog photoActionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        getPermissions();

        setListeners();
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (cameraPermission != PackageManager.PERMISSION_GRANTED
                    && writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_ALL);
            } else {
                if (cameraPermission != PackageManager.PERMISSION_GRANTED) {//Request runtime permission for camera access
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                            REQUEST_IMAGE_CAPTURE);
                }
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    //Request runtime permission for external storage writing permission
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_EXTERNAL);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (photoActionDialog != null)
                        photoActionDialog.dismiss();
                    else
                        finish();
                    Toast.makeText(this, "Please allow permissions for uploading photo.", Toast.LENGTH_SHORT).show();

                }
                return;
            }
            case REQUEST_IMAGE_CAPTURE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (photoActionDialog != null)
                        photoActionDialog.dismiss();
                    Toast.makeText(this, "Please allow CAMERA permission.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (photoActionDialog != null)
                        photoActionDialog.dismiss();
                    Toast.makeText(this, "Please allow WRITE EXTERNAL permission.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * Check if the device has permissions for camera and write external storage
     * @return true if it does, false otherwise. For devices lower than Marshmallow, it will assume the device has the permission from Manifest file
     */
    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int cameraPermission = checkSelfPermission(android.Manifest.permission.CAMERA);
            int writeExternalStoragePermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            return cameraPermission != PackageManager.PERMISSION_GRANTED && writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED;
        }
        return true;

    }

    private void init() {
        profilePhotoIV = findViewById(R.id.profilePhoto_id);

        firstNameET = findViewById(R.id.firstNameText_id);
        lastNameET = findViewById(R.id.lastNameText_id);
        emailET = findViewById(R.id.emailAddressText_id);
        passwordET = findViewById(R.id.passwordText_id);

        signUpBtn = findViewById(R.id.signUpBtn_id);

        mPrgressDialog = new ProgressDialog(this);

        presenter = new SignUpPresenter(this);
    }

    private void setListeners() {
        profilePhotoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoActionDialog();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        String firstNameInput = firstNameET.getText().toString();
        String lastNameInput = lastNameET.getText().toString();
        String emailInput = emailET.getText().toString();
        String passwordInput = passwordET.getText().toString();

        if (!TextUtils.isEmpty(emailInput) && !TextUtils.isEmpty(passwordInput)
                && !TextUtils.isEmpty(firstNameInput) && !TextUtils.isEmpty(lastNameInput)) {
            initSignUp(firstNameInput, lastNameInput, emailInput, passwordInput);
        } else {
            if (TextUtils.isEmpty(firstNameInput))
                firstNameET.setError(getString(R.string.firstNamePrompt));
            else if (TextUtils.isEmpty(lastNameInput))
                lastNameET.setError(getString(R.string.lastNamePrompt));
            else if (TextUtils.isEmpty(emailInput))
                emailET.setError(getString(R.string.emailPrompt));
            else
                passwordET.setError(getString(R.string.passwordPrompt));
        }
    }

    private void initSignUp(String firstName, String lastName, String email, String password) {
        mPrgressDialog.setMessage("Signing up account...");
        mPrgressDialog.show();
        presenter.signUp(this, firstName, lastName, email, password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap profileBitmap = (Bitmap) extras.get("data");
            presenter.uploadCapturedPhoto(this, profileBitmap, "1");
        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            presenter.uploadGalleryPhotoTo(this, imageUri, "1");
        }
    }

    private void showPhotoActionDialog() {
        if (!hasPermissions()) {
            getPermissions();
        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SignUpActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_pick_photos, null);
        LinearLayout galleryLL = mView.findViewById(R.id.galleryLL);
        LinearLayout cameraLL = mView.findViewById(R.id.cameraLL);

        galleryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY_PHOTO);
            }
        });

        cameraLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        mBuilder.setView(mView);
        photoActionDialog = mBuilder.create();
        photoActionDialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onSignUpSuccess(FirebaseUser user) {
        mPrgressDialog.dismiss();
        MainActivity.currentUser = user;
        Toast.makeText(this, R.string.successSignUp, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignUpFailure(String error) {
        mPrgressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoUploadSuccess(Uri imageUri) {
        photoActionDialog.dismiss();
        profilePhotoIV.setImageURI(imageUri);
        Toast.makeText(this, "Photo uploaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoUploadFailure(String error) {
        photoActionDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
