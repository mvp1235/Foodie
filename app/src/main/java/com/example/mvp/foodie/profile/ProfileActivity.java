package com.example.mvp.foodie.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mvp.foodie.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    private AppCompatTextView name, email, location, postCount, friendCount;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setUpListeners();

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
    }

    private void setUpListeners() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Set action upon clicking on menu items
        switch (id) {
            case R.id.profile_edit:
//                Toast.makeText(getApplicationContext(), "Editing Profile", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
