package com.example.mvp.foodie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

public class SignInActivity extends AppCompatActivity {

    final static int CREATE_ACCOUNT_CODE = 100;

    AppCompatButton facebookLoginBtn, googleLoginBtn, loginBtn, createAccountBtn, forgotPasswordBtn;
    AppCompatEditText usernameET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Referencing the buttons
        facebookLoginBtn = findViewById(R.id.facebookLogin_id);
        googleLoginBtn = findViewById(R.id.googleLogin_id);
        loginBtn = findViewById(R.id.loginBtn_id);
        createAccountBtn = findViewById(R.id.createAccountBtn_id);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn_id);

        //Referencing the EditTexts
        usernameET = findViewById(R.id.usernameText_id);
        passwordET = findViewById(R.id.passwordText_id);

        //Setting onclick listener for the buttons
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, CREATE_ACCOUNT_CODE);
            }
        });



    }
}
