package com.example.mvp.foodie;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainFeedActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    PostPhotoSlideShowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager_id);
        adapter = new PostPhotoSlideShowAdapter(this);

//        viewPager.setAdapter(adapter);
    }
}
