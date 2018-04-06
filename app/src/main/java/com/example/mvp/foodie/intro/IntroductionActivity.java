package com.example.mvp.foodie.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.MainActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.signin.SignInActivity;
import com.example.mvp.foodie.signup.SignUpActivity;
import com.google.firebase.auth.FirebaseUser;

public class IntroductionActivity extends BaseActivity implements IntroductionContract.View{

    private AppCompatButton signInBtn, signUpBtn;
    private IntroductionContract.Presenter presenter;

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

        presenter = new IntroductionPresenter(this);
        presenter.checkFirebaseAuth();
    }

    @Override
    public void isLoggedIn(FirebaseUser user) {
        Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
