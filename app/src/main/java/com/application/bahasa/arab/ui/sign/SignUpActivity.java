package com.application.bahasa.arab.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.application.bahasa.arab.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtStudentName,edtStudentIdNumber,edtEmail,edtPassword,edtConfirmPassword;
    private ImageButton btnShowPassword,btnHidePassword,btnShowConfirmPassword,btnHideConfirmPassword;
    private Button btnSignUp,btnSignIn;
    private CheckBox checkBox;
    private TextView tvTermsAndConditions,tvSignUpIn,tvSignInUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_in);

        edtStudentName = findViewById(R.id.edtStudentName);
        edtStudentIdNumber = findViewById(R.id.edtStudentIdNumber);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnShowPassword = findViewById(R.id.showPassword);
        btnHidePassword = findViewById(R.id.hidePassword);
        btnShowConfirmPassword = findViewById(R.id.showConfirmPassword);
        btnHideConfirmPassword = findViewById(R.id.hideConfirmPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        checkBox = findViewById(R.id.checkBok);

        tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        tvSignUpIn = findViewById(R.id.tvSignUpIn);
        tvSignInUp = findViewById(R.id.tvSignInUp);

        btnHidePassword.setOnClickListener(v -> {
            btnHidePassword.setVisibility(View.INVISIBLE);
            btnShowPassword.setVisibility(View.VISIBLE);
            edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        });
        btnShowPassword.setOnClickListener(v -> {
            btnHidePassword.setVisibility(View.VISIBLE);
            btnShowPassword.setVisibility(View.INVISIBLE);
            edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        });

        btnHideConfirmPassword.setOnClickListener(v -> {
            btnHideConfirmPassword.setVisibility(View.INVISIBLE);
            btnShowConfirmPassword.setVisibility(View.VISIBLE);
            edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        });
        btnShowConfirmPassword.setOnClickListener(v -> {
            btnHideConfirmPassword.setVisibility(View.VISIBLE);
            btnShowConfirmPassword.setVisibility(View.INVISIBLE);
            edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        });

        tvSignUpIn.setOnClickListener(v -> {
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        });
    }
}