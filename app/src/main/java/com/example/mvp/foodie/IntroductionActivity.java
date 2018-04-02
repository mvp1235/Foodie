package com.example.mvp.foodie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.mvp.foodie.signin.SignInActivity;
import com.example.mvp.foodie.signup.SignUpActivity;

public class IntroductionActivity extends AppCompatActivity {

    private AppCompatButton signInBtn, signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        signInBtn = findViewById(R.id.signInBtn_id);
        signUpBtn = findViewById(R.id.signUpBtn_id);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroductionActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroductionActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
