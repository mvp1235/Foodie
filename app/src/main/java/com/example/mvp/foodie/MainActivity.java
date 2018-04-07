package com.example.mvp.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mvp.foodie.models.User;
import com.example.mvp.foodie.navigation_drawer.DrawerContract;
import com.example.mvp.foodie.navigation_drawer.DrawerPresenter;
import com.example.mvp.foodie.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerContract.View {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView navigation;

    MenuItem searchMenu, addPostMenu, sendMessageMenu;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new MainFeedFragment();
                    toolbar.setTitle(R.string.feeds);
                    searchMenu.setVisible(true);
                    addPostMenu.setVisible(true);
                    sendMessageMenu.setVisible(false);

                    break;
                case R.id.navigation_messages:
                    selectedFragment = new MessageFragment();
                    toolbar.setTitle(R.string.messages);

                    searchMenu.setVisible(false);
                    addPostMenu.setVisible(false);
                    sendMessageMenu.setVisible(true);
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new NotificationFragment();
                    toolbar.setTitle(R.string.notifications);

                    searchMenu.setVisible(false);
                    addPostMenu.setVisible(false);
                    sendMessageMenu.setVisible(false);

                    break;
            }

            setFragment(selectedFragment);
            return true;
        }
    };

    private DrawerContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setmAuth(FirebaseAuth.getInstance());

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Set default option to the Home view fragmemt.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new MainFeedFragment());
        transaction.commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.navigation_drawer);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.closer_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        presenter = new DrawerPresenter(this);
        presenter.loadData(getmAuth().getCurrentUser().getUid());

    }


    @Override
    public void onBackPressed() {
        //If drawer is open, pressing back key will close it instead of exiting the application
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (navigation.getSelectedItemId() != R.id.navigation_home) { //If tabs other than home tab are currently selected, pressing back key will first take it to the home tab
            Fragment selectedFragment = new MainFeedFragment();
            setFragment(selectedFragment);
            navigation.setSelectedItemId(R.id.navigation_home);
            return;
        } else {
            //Log the user out
            //SHOW CONFIRM DIALOG LATER/////////////////////////
            getmAuth().signOut();
            setFirebaseUser(null);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.profile_id:
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.settings_id:

                break;
            case R.id.logout_id:
                //Log user out
                getmAuth().signOut();
                setFirebaseUser(null);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        //return true will highlight chosen menu, false will do nothing
        return false;
    }


    /**
     * Set the main fragment view to a certain fragment determined by what the user had chosen
     * @param selectedFragment the selected fragment
     */
    public void setFragment(Fragment selectedFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, selectedFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        searchMenu = menu.findItem(R.id.menu_search);
        addPostMenu = menu.findItem(R.id.menu_add_post);
        sendMessageMenu = menu.findItem(R.id.menu_send_message);

        sendMessageMenu.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Set action upon clicking on menu items
        switch (id) {
            case R.id.menu_search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_add_post:
                Toast.makeText(this, "Add post", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_send_message:
                Toast.makeText(this, "Send message", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadDataSuccess(User user) {
        //Customize navigation drawer header with user's information
        View headerView = navigationView.getHeaderView(0);
        de.hdodenhof.circleimageview.CircleImageView userProfilePhoto = headerView.findViewById(R.id.userProfilePhoto_id);
        AppCompatTextView userFullName = headerView.findViewById(R.id.userFullName_id);
        AppCompatTextView userEmail = headerView.findViewById(R.id.userEmail_id);

        userEmail.setText(user.getEmail());
        userFullName.setText(user.getFullName());

        if (user.getProfileURL() != null)
            Picasso.get().load(user.getProfileURL()).into(userProfilePhoto);
        else
            Picasso.get().load("http://www.personalbrandingblog.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640-300x300.png").into(userProfilePhoto);
    }

    @Override
    public void onLoadDataFailure(String error) {
        Toast.makeText(this, R.string.loadDataFailed, Toast.LENGTH_SHORT).show();
    }
}


