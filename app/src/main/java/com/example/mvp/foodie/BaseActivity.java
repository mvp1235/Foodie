package com.example.mvp.foodie;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    public static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;

    public BaseActivity() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }
}
