package com.example.mvp.foodie.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.MainActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.signup.SignUpActivity;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends BaseActivity implements SignInContract.View {

    final static int CREATE_ACCOUNT_CODE = 100;

    AppCompatButton facebookLoginBtn, googleLoginBtn, loginBtn, createAccountBtn, forgotPasswordBtn;
    AppCompatEditText usernameET, passwordET;

    private SignInContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
        setUpListeners();

        //check whether user is logged in or not.
        //If yes, go to feed activity, else show login page
        presenter.checkFirebaseAuth();
    }

    private void init() {
        //Referencing the buttons
        facebookLoginBtn = findViewById(R.id.facebookLogin_id);
        googleLoginBtn = findViewById(R.id.googleLogin_id);
        loginBtn = findViewById(R.id.loginBtn_id);
        createAccountBtn = findViewById(R.id.createAccountBtn_id);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn_id);

        //Referencing the EditTexts
        usernameET = findViewById(R.id.usernameText_id);
        passwordET = findViewById(R.id.passwordText_id);

        presenter = new SignInPresenter(this);
    }

    private void setUpListeners() {
        //Setting onclick listener for the buttons
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, CREATE_ACCOUNT_CODE);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        String emailInput = usernameET.getText().toString();
        String passwordInput = passwordET.getText().toString();

        if (!TextUtils.isEmpty(emailInput) && !TextUtils.isEmpty(passwordInput)) {
            initSignIn(emailInput, passwordInput);
        } else {
            if (TextUtils.isEmpty(emailInput))
                usernameET.setError(getString(R.string.emailPrompt));
            else
                passwordET.setError(getString(R.string.passwordPrompt));

        }
    }

    private void initSignIn(String email, String password) {
        presenter.signIn(this, email, password);
    }

    @Override
    public void onSignInSuccess(FirebaseUser user) {
        setFirebaseUser(user);
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        Toast.makeText(this, "Signed in successfully as " + user.getEmail(), Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onSignInFailure(String error) {
        setFirebaseUser(null);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isLoggedIn(FirebaseUser user) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
