package com.application.bahasa.arab.ui.sign;

import android.content.Intent;
import android.database.Cursor;
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

public class SignInActivity extends AppCompatActivity {

    private TextView tvEmail,tvPassword;
    private EditText edtEmail,edtPassword;
    private ImageButton btnShowPassword,btnHidePassword;
    private Button btnSignUp,btnSignIn;
    private TextView tvTermsAndConditions,tvSignInUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_in);

        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setVisibility(View.INVISIBLE);
        TextView tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setVisibility(View.VISIBLE);

        TextView tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentName.setVisibility(View.GONE);
        TextView tvStudentIdNumber = findViewById(R.id.tvStudentIdNumber);
        tvStudentIdNumber.setVisibility(View.GONE);
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        TextView tvConfirmPassword = findViewById(R.id.tvConfirmPassword);
        tvConfirmPassword.setVisibility(View.GONE);

        EditText edtStudentName = findViewById(R.id.edtStudentName);
        edtStudentName.setVisibility(View.GONE);
        EditText edtStudentIdNumber = findViewById(R.id.edtStudentIdNumber);
        edtStudentIdNumber.setVisibility(View.GONE);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        EditText edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtConfirmPassword.setVisibility(View.GONE);

        btnShowPassword = findViewById(R.id.showPassword);
        btnHidePassword = findViewById(R.id.hidePassword);
        ImageButton btnShowConfirmPassword = findViewById(R.id.showConfirmPassword);
        btnShowConfirmPassword.setVisibility(View.GONE);
        ImageButton btnHideConfirmPassword = findViewById(R.id.hideConfirmPassword);
        btnHideConfirmPassword.setVisibility(View.GONE);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setVisibility(View.INVISIBLE);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setVisibility(View.VISIBLE);

        CheckBox checkBox = findViewById(R.id.checkBok);
        checkBox.setVisibility(View.GONE);

        tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setVisibility(View.GONE);
        TextView tvSignUpIn = findViewById(R.id.tvSignUpIn);
        tvSignUpIn.setVisibility(View.GONE);
        tvSignInUp = findViewById(R.id.tvSignInUp);
        tvSignInUp.setVisibility(View.VISIBLE);


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

        tvSignInUp.setOnClickListener(v -> {
            startActivity(new Intent(this,SignUpActivity.class));
            finish();
        });
    }
}