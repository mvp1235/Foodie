package com.example.mvp.foodie.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.MainActivity;
import com.example.mvp.foodie.R;
import com.google.firebase.auth.FirebaseUser;

import static com.example.mvp.foodie.signin.SignInActivity.RESULT_CLOSE_ALL;

public class SignUpActivity extends BaseActivity implements SignUpContract.View {
    private static final int VIEW_FEED = 50001;

    AppCompatImageView profilePhotoIV;
    AppCompatEditText firstNameET, lastNameET, emailET, passwordET;
    AppCompatButton signUpBtn;

    ProgressDialog mPrgressDialog;
    private SignUpPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        setListeners();
    }

    private void init() {
        profilePhotoIV = findViewById(R.id.profilePhoto_id);

        firstNameET = findViewById(R.id.firstNameText_id);
        lastNameET = findViewById(R.id.lastNameText_id);
        emailET = findViewById(R.id.emailAddressText_id);
        passwordET = findViewById(R.id.passwordText_id);

        signUpBtn = findViewById(R.id.signUpBtn_id);

        mPrgressDialog = new ProgressDialog(this);

        presenter = new SignUpPresenter(this);
    }

    private void setListeners() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        String firstNameInput = firstNameET.getText().toString();
        String lastNameInput = lastNameET.getText().toString();
        String emailInput = emailET.getText().toString();
        String passwordInput = passwordET.getText().toString();

        if (!TextUtils.isEmpty(emailInput) && !TextUtils.isEmpty(passwordInput)
                && !TextUtils.isEmpty(firstNameInput) && !TextUtils.isEmpty(lastNameInput)) {
            initSignUp(firstNameInput, lastNameInput, emailInput, passwordInput);
        } else {
            if (TextUtils.isEmpty(firstNameInput))
                firstNameET.setError(getString(R.string.firstNamePrompt));
            else if (TextUtils.isEmpty(lastNameInput))
                lastNameET.setError(getString(R.string.lastNamePrompt));
            else if (TextUtils.isEmpty(emailInput))
                emailET.setError(getString(R.string.emailPrompt));
            else
                passwordET.setError(getString(R.string.passwordPrompt));
        }
    }

    private void initSignUp(String firstName, String lastName, String email, String password) {
        mPrgressDialog.setMessage("Signing up account...");
        mPrgressDialog.show();
        presenter.signUp(this, firstName, lastName, email, password);
    }

    @Override
    public void onSignUpSuccess(FirebaseUser user) {
        mPrgressDialog.dismiss();
        setFirebaseUser(user);
        Toast.makeText(this, R.string.successSignUp, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivityForResult(intent, VIEW_FEED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIEW_FEED && resultCode == RESULT_CLOSE_ALL) {
            finish();
        }
    }

    @Override
    public void onSignUpFailure(String error) {
        mPrgressDialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}
