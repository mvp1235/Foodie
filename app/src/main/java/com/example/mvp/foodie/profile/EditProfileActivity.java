package com.example.mvp.foodie.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.User;


public class EditProfileActivity extends BaseActivity implements ProfileContract.EditView{

    private AppCompatEditText firstName, lastName, email;
    private AppCompatButton editBtn;

    Toolbar toolbar;

    private ProgressDialog mPrgressDialog;

    private ProfileContract.EditPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        setUpListeners();

        presenter = new EditProfilePresenter(this);
        presenter.loadData(getIntent());
    }

    private void initViews() {
        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.editProfile);

        firstName = findViewById(R.id.firstNameText_id);
        lastName = findViewById(R.id.lastNameText_id);
        email = findViewById(R.id.emailText_id);
        editBtn = findViewById(R.id.editBtn_id);
        mPrgressDialog = new ProgressDialog(this);
    }

    private void setUpListeners() {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        String firstNameInput = firstName.getText().toString();
        String lastNameInput = lastName.getText().toString();
        String emailInput = email.getText().toString();

        if (!TextUtils.isEmpty(emailInput) && !TextUtils.isEmpty(firstNameInput)
                && !TextUtils.isEmpty(firstNameInput) && !TextUtils.isEmpty(lastNameInput)) {
            initEditProfile(firstNameInput, lastNameInput, emailInput);
        } else {
            if (TextUtils.isEmpty(firstNameInput))
                firstName.setError(getString(R.string.firstNamePrompt));
            else if (TextUtils.isEmpty(lastNameInput))
                lastName.setError(getString(R.string.lastNamePrompt));
            else
                email.setError(getString(R.string.emailPrompt));
        }
    }


    private void initEditProfile(String firstName, String lastName, String email) {
        mPrgressDialog.setMessage("Editing profile...");
        mPrgressDialog.show();
        presenter.edit(this, getmAuth().getCurrentUser().getUid(), firstName, lastName, email);
    }

    @Override
    public void onEditSuccess(User user) {
        mPrgressDialog.hide();
        Toast.makeText(this, R.string.editSuccessfully, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onEditFailure(String error) {
        mPrgressDialog.hide();
        Toast.makeText(this, R.string.editFailed, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onLoadSuccess(String firstName, String lastName, String email) {
        this.firstName.setText(firstName);
        this.lastName.setText(lastName);
        this.email.setText(email);
    }

    @Override
    public void onLoadFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}