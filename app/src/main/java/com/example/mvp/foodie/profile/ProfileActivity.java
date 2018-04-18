package com.example.mvp.foodie.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Notification;
import com.example.mvp.foodie.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mvp.foodie.UtilHelper.EMAIL;
import static com.example.mvp.foodie.UtilHelper.FULL_NAME;
import static com.example.mvp.foodie.UtilHelper.REQUEST_ALL;
import static com.example.mvp.foodie.UtilHelper.REQUEST_CODE;
import static com.example.mvp.foodie.UtilHelper.REQUEST_EDIT_PROFILE;
import static com.example.mvp.foodie.UtilHelper.REQUEST_GALLERY_PHOTO;
import static com.example.mvp.foodie.UtilHelper.REQUEST_IMAGE_CAPTURE;
import static com.example.mvp.foodie.UtilHelper.REQUEST_WRITE_EXTERNAL;
import static com.example.mvp.foodie.UtilHelper.USER_ID;
import static com.example.mvp.foodie.UtilHelper.VIEW_MY_PROFILE;
import static com.example.mvp.foodie.UtilHelper.VIEW_OTHER_PROFILE;

public class ProfileActivity extends BaseActivity implements ProfileContract.View, ProfileContract.onUploadListener {
    Toolbar toolbar;
    private AppCompatTextView name, email, location, postCount, friendCount;
    private CircleImageView profileImage;
    private AppCompatButton addFriendBtn, unFriendBtn, acceptBtn, declineBtn;

    private AlertDialog photoActionDialog;

    private ProfileContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        getPermissions();
        setUpListeners();
        presenter = new ProfilePresenter(this);
        loadData();

    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent.getIntExtra(REQUEST_CODE, 0) == VIEW_MY_PROFILE) {
            presenter.loadDataFromFirebase(getmAuth().getCurrentUser().getUid());

            //Show/Hide appropriate fields
            //Hide all irrelevant field
            addFriendBtn.setVisibility(View.GONE);
            unFriendBtn.setVisibility(View.GONE);
            acceptBtn.setVisibility(View.GONE);
            declineBtn.setVisibility(View.GONE);

        } else if (intent.getIntExtra(REQUEST_CODE, 0) == VIEW_OTHER_PROFILE) {
            presenter.loadDataFromFirebase(intent.getStringExtra(USER_ID));

            //Show/Hide appropriate fields
            //Hide all irrelevant field
            if(!intent.getStringExtra(USER_ID).equals(getmAuth().getCurrentUser().getUid()))
                addFriendBtn.setVisibility(View.VISIBLE);
            else
                addFriendBtn.setVisibility(View.GONE);
            unFriendBtn.setVisibility(View.GONE);
            acceptBtn.setVisibility(View.GONE);
            declineBtn.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "There is a problem with the server. Please reload the application.", Toast.LENGTH_SHORT).show();
        }

        //FOR NOW, WON"T BE IMPLEMENTING FRIEND FEATURE
        addFriendBtn.setVisibility(View.GONE);
        unFriendBtn.setVisibility(View.GONE);
        acceptBtn.setVisibility(View.GONE);
        declineBtn.setVisibility(View.GONE);
        ////////////////////////////////////////////////
        //REMOVE THIS WHEN BACK TO IMPLEMENTING FRIEND FEATURE
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

    private void initViews() {
        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.profile);

        name = findViewById(R.id.profileName_id);
        email = findViewById(R.id.profileEmail_id);
        location = findViewById(R.id.profileLocation_id);
        postCount = findViewById(R.id.profilePostCount_id);
        friendCount = findViewById(R.id.profileFriendCount_id);
        profileImage = findViewById(R.id.profilePhoto_id);

        addFriendBtn = findViewById(R.id.addFriendBtn_id);
        unFriendBtn = findViewById(R.id.unFriendBtn_id);
        acceptBtn = findViewById(R.id.acceptRequestBtn_id);
        declineBtn = findViewById(R.id.removeRequestBtn_id);
    }

    private void setUpListeners() {
        final Intent receivedIntent = getIntent();
        //only allow user to change profile photo if they are viewing their own profile
        if (receivedIntent.getIntExtra(REQUEST_CODE, 0) == VIEW_MY_PROFILE) {
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhotoActionDialog();
                }
            });
        } else if (receivedIntent.getStringExtra(USER_ID).equals(getmAuth().getCurrentUser().getUid())) {
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhotoActionDialog();
                }
            });

        }

//        addFriendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (addFriendBtn.getText().toString().equalsIgnoreCase("Add Friend")) {
//                    addFriendBtn.setText(R.string.cancel_request);
//                    sendFriendRequest(getmAuth().getCurrentUser().getUid(), receivedIntent.getStringExtra(USER_ID));
//                } else {
//                    addFriendBtn.setText(R.string.add_friend);
//
//                }
//
//            }
//        });



    }


    private void sendFriendRequest(final String fromID, final String toID) {
        final DatabaseReference notificationRef = getmDatabase().child("Notifications");
        final DatabaseReference userRef = getmDatabase().child("Users");
        final String newNotificationID = notificationRef.push().getKey();

        final Notification notification = new Notification();
        notification.setContent("sent you a friend request.");
        notification.setnID(newNotificationID);

        userRef.child(fromID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Obtain fromUser data to store in notification
                final User fromUser = dataSnapshot.getValue(User.class);
                notification.setPhotoURL(fromUser.getProfileURL());
                notification.setUserName(fromUser.getFullName());


                //Save notification to user's notification list
                userRef.child(toID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User toUser = dataSnapshot.getValue(User.class);
                        toUser.addNotification(notification);

                        userRef.child(toID).setValue(toUser);
                        userRef.child(fromID).setValue(fromUser);

                        //Save friend request notification to Notifications database
                        notificationRef.child(newNotificationID).setValue(notification);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap profileBitmap = (Bitmap) extras.get("data");
            presenter.uploadCapturedPhoto(this, profileBitmap, getmAuth().getCurrentUser().getUid());
        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            presenter.uploadGalleryPhotoTo(this, imageUri, getmAuth().getCurrentUser().getUid());
        } else if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            String fullName = data.getStringExtra(FULL_NAME);
            String email = data.getStringExtra(EMAIL);


        }
    }


    private void showPhotoActionDialog() {
        if (!hasPermissions()) {
            getPermissions();
        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_toolbar_items, menu);
        MenuItem editMenu = menu.findItem(R.id.profile_edit);

        //If viewing other's profile, hide edit menu to prevent you from editing other people's profile
        if (getIntent().getIntExtra(REQUEST_CODE, 0) == VIEW_OTHER_PROFILE) {
            if (!getIntent().getStringExtra(USER_ID).equals(getmAuth().getCurrentUser().getUid()))
                editMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Set action upon clicking on menu items
        switch (id) {
            case R.id.profile_edit:
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra(FULL_NAME, name.getText().toString());
                intent.putExtra(EMAIL, email.getText().toString());
                startActivityForResult(intent, REQUEST_EDIT_PROFILE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadDataSuccess(User user) {
        name.setText(user.getFullName());
        email.setText(user.getEmail());
        postCount.setText(user.getPostCount() + " posts");
        friendCount.setText(user.getFriendCount() + " friends");
        if (user.getProfileURL() != null)
            Picasso.get().load(user.getProfileURL()).into(profileImage);
        else
            Picasso.get().load("http://www.personalbrandingblog.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640-300x300.png").into(profileImage);
    }

    @Override
    public void onLoadDataFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPhotoUploadSuccess(Uri imageUri) {
        photoActionDialog.dismiss();
        profileImage.setImageURI(imageUri);
        Toast.makeText(this, "Photo uploaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhotoUploadFailure(String error) {
        photoActionDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
