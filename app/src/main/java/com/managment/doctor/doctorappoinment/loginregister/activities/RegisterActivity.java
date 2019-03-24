package com.managment.doctor.doctorappoinment.loginregister.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.imageauth.ImageAuthActivity;
import com.managment.doctor.doctorappoinment.loginregister.helpers.InputValidation;
import com.managment.doctor.doctorappoinment.loginregister.model.Doctor;
import com.managment.doctor.doctorappoinment.loginregister.sql.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;


    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private Doctor doctor;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        doctor = new Doctor();

    }


    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        loginByMail();
//
//        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {
//
//            doctor.setName(textInputEditTextName.getText().toString().trim());
//            doctor.setEmail(textInputEditTextEmail.getText().toString().trim());
//            doctor.setPassword(textInputEditTextPassword.getText().toString().trim());
//
//
//            if (databaseHelper.addUser(doctor))
//            {
//                Toast.makeText(activity, getString(R.string.success_message), Toast.LENGTH_SHORT).show();
//                finish();
//            }
//
//
//        } else {
//            // Snack Bar to show error message that record already exists
//            Toast.makeText(activity,  getString(R.string.error_email_exists), Toast.LENGTH_SHORT).show();
//        }


    }

    private void loginByMail() {
        doctor.setName(textInputEditTextName.getText().toString().trim());
        doctor.setEmail(textInputEditTextEmail.getText().toString().trim());
        doctor.setPassword(textInputEditTextPassword.getText().toString().trim());
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(doctor.getEmail(), doctor.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            createUser();
                        }
                    }
                });
    }

    private void createUser() {
        DatabaseReference mFirebaseInstance = FirebaseDatabase.getInstance().getReference("DoctorsList");
        String userId = mFirebaseInstance.child(FirebaseAuth.getInstance().getUid()).getKey();
        mFirebaseInstance.child(userId).setValue(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                if (DatabaseHelper.getInstance(RegisterActivity.this).addUser(doctor)) {
                    startActivity(new Intent(RegisterActivity.this, ImageAuthActivity.class));
                    finish();
                } else Toast.makeText(RegisterActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);

            }
        });


    }
}
