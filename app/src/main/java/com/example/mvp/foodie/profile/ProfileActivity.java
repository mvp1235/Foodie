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
import com.example.mvp.foodie.friend.FriendContract;
import com.example.mvp.foodie.friend.FriendListActivity;
import com.example.mvp.foodie.friend.FriendPresenter;
import com.example.mvp.foodie.friend.FriendRequestsActivity;
import com.example.mvp.foodie.models.FriendRequest;
import com.example.mvp.foodie.models.User;
import com.example.mvp.foodie.post.AllUserPostActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
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

public class ProfileActivity extends BaseActivity implements ProfileContract.View, FriendContract.View {
    Toolbar toolbar;
    private AppCompatTextView nameTV, emailTV, postCountTV, friendCountTV;
    private CircleImageView profileImage;
    private AppCompatButton viewFriendRequestsBtn, addFriendBtn;

    private AlertDialog photoActionDialog;

    private ProfileContract.Presenter presenter;
    private FriendContract.Presenter friendPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        getPermissions();
        setUpListeners();
        presenter = new ProfilePresenter(this);
        friendPresenter = new FriendPresenter(this);
        loadData();

    }

    private void loadData() {
        Intent intent = getIntent();

        //User viewing his/her own profile
        if (intent.getIntExtra(REQUEST_CODE, 0) == VIEW_MY_PROFILE) {
            presenter.loadDataFromFirebase(getmAuth().getCurrentUser().getUid());

            viewFriendRequestsBtn.setVisibility(View.VISIBLE);
            addFriendBtn.setVisibility(View.GONE);

        } else if (intent.getIntExtra(REQUEST_CODE, 0) == VIEW_OTHER_PROFILE) { //User viewing others' profiles
            presenter.loadDataFromFirebase(intent.getStringExtra(USER_ID));

            //Hide view requests button when viewing others' profiles
            if (intent.getStringExtra(USER_ID).equals(getmAuth().getCurrentUser().getUid())) {  //user viewing his/her own profile
                viewFriendRequestsBtn.setVisibility(View.VISIBLE);
                addFriendBtn.setVisibility(View.GONE);
            } else {
                viewFriendRequestsBtn.setVisibility(View.GONE);
                addFriendBtn.setVisibility(View.VISIBLE);
                friendPresenter.checkUserFriendship(getmAuth().getCurrentUser().getUid(), intent.getStringExtra(USER_ID));
                friendPresenter.checkSentFriendRequest(getmAuth().getCurrentUser().getUid(), intent.getStringExtra(USER_ID));
            }

        } else {
            Toast.makeText(this, "There is a problem with the server. Please reload the application.", Toast.LENGTH_SHORT).show();
        }

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

        nameTV = findViewById(R.id.profileName_id);
        emailTV = findViewById(R.id.profileEmail_id);
        postCountTV = findViewById(R.id.profilePostCount_id);
        friendCountTV = findViewById(R.id.profileFriendCount_id);
        profileImage = findViewById(R.id.profilePhoto_id);

        viewFriendRequestsBtn = findViewById(R.id.viewFriendRequestBtn_id);
        addFriendBtn = findViewById(R.id.addFriendBtn_id);
    }

    private void setUpListeners() {
        final Intent receivedIntent = getIntent();
        final String fromUserID = getmAuth().getCurrentUser().getUid();
        final String toUserID = receivedIntent.getStringExtra(USER_ID);

        //only allow user to change profile photo if they are viewing their own profile
        if (receivedIntent.getIntExtra(REQUEST_CODE, 0) == VIEW_MY_PROFILE) {
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhotoActionDialog();
                }
            });
        } else if (toUserID.equals(fromUserID)) {
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhotoActionDialog();
                }
            });

        }

        postCountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String userID = receivedIntent.getStringExtra(USER_ID);

            Intent intent = new Intent(ProfileActivity.this, AllUserPostActivity.class);
            if (userID != null)
                intent.putExtra(USER_ID, userID);
            else
                intent.putExtra(USER_ID, getmAuth().getCurrentUser().getUid());
            startActivity(intent);
            }
        });

        friendCountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = receivedIntent.getStringExtra(USER_ID);
                Intent intent = new Intent(ProfileActivity.this, FriendListActivity.class);

                if (userID != null)
                    intent.putExtra(USER_ID, userID);
                else
                    intent.putExtra(USER_ID, getmAuth().getCurrentUser().getUid());
                startActivity(intent);
            }
        });

        viewFriendRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(ProfileActivity.this, FriendRequestsActivity.class);
                startActivity(intent);
            }
        });

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = addFriendBtn.getText().toString();

                if (text.equals(getString(R.string.add_friend))) {
                    addFriendBtn.setText(getString(R.string.cancel));
                    friendPresenter.sendFriendRequest(fromUserID, toUserID);
                } else if (text.equals(getString(R.string.cancel))){
                    addFriendBtn.setText(getString(R.string.add_friend));
                    friendPresenter.cancelFriendRequest(fromUserID, toUserID);
                } else if (text.equals(getString(R.string.unfriend))){
                    addFriendBtn.setText(getString(R.string.add_friend));
                    friendPresenter.removeFriendship(fromUserID, toUserID);
                }

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
            nameTV.setText(fullName);
            emailTV.setText(email);
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
                intent.putExtra(FULL_NAME, nameTV.getText().toString());
                intent.putExtra(EMAIL, emailTV.getText().toString());
                startActivityForResult(intent, REQUEST_EDIT_PROFILE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadDataSuccess(final User user) {
        nameTV.setText(user.getFullName());
        emailTV.setText(user.getEmail());
        postCountTV.setText(user.getPostCount() + " posts");
        friendCountTV.setText(user.getFriendCount() + " friends");

        if (user.getProfileURL() != null) {
            Picasso.get().load(user.getProfileURL())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {}

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(user.getProfileURL()).into(profileImage);
                        }
                    });
        } else {
            Picasso.get().load("http://www.personalbrandingblog.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640-300x300.png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {}

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load("http://www.personalbrandingblog.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640-300x300.png").into(profileImage);
                        }
                    });
        }

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

    @Override
    public void onCheckUserFriendshipSuccess(boolean friend) {
        if (friend) {
            addFriendBtn.setText(R.string.unfriend);
        } else {
            addFriendBtn.setText(R.string.add_friend);
        }
    }

    @Override
    public void onCheckUserFriendshipFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckSentRequest(boolean sent) {
        if (sent) {
            addFriendBtn.setText(R.string.cancel);
        }
    }

    @Override
    public void onCheckSentFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendRequestSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "Sent a friend request to " + toUser.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendRequestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFriendshipSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "You are no longer friend with " + toUser.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFriendshipFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelRequestSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "Canceled friend request from " + toUser.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelRequestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptRequestSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "You and " + toUser.getFullName() + " have became friends.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptRequestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineRequestSuccess(User fromUser, User toUser) {
        Toast.makeText(this, "You have declined friend request from " + toUser.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineRequestFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadRequestsSuccess(FriendRequest friendRequest) {}

    @Override
    public void onLoadRequestsFailure(String error) {}
};
