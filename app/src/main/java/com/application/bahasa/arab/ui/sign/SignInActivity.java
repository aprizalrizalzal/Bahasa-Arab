package com.application.bahasa.arab.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.ui.HomeTabActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView tvPassword;
    private TextInputLayout tiEmail,tiPassword;
    private TextView tvSignInUp,tvForgotPassword;
    private String email,password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_in);

        AdView adViewUnit = findViewById(R.id.adViewSign);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewUnit.loadAd(adRequest);

        auth=FirebaseAuth.getInstance();

        if (auth.getCurrentUser()!=null) {
            startActivity(new Intent(getApplication(), HomeTabActivity.class));
            finish();
        }

        TextView tvUserName = findViewById(R.id.tvUserName);
        tvUserName.setVisibility(View.INVISIBLE);
        tvPassword = findViewById(R.id.tvPassword);
        TextView tvConfirmPassword = findViewById(R.id.tvConfirmPassword);
        tvConfirmPassword.setVisibility(View.GONE);
        TextInputLayout tiUserName = findViewById(R.id.tiUserName);
        tiUserName.setVisibility(View.GONE);
        tiEmail = findViewById(R.id.tiEmail);
        tiPassword = findViewById(R.id.tiPassword);
        TextInputLayout tiConfirmPassword = findViewById(R.id.tiConfirmPassword);
        tiConfirmPassword.setVisibility(View.GONE);


        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setVisibility(View.INVISIBLE);
        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setVisibility(View.VISIBLE);
        Button btnSend = findViewById(R.id.btnSend);

        CheckBox checkBox = findViewById(R.id.checkBok);
        checkBox.setVisibility(View.GONE);

        TextView tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        tvTermsAndConditions.setVisibility(View.GONE);
        TextView tvSignUpIn = findViewById(R.id.tvSignUpIn);
        tvSignUpIn.setVisibility(View.GONE);
        tvSignInUp = findViewById(R.id.tvSignInUp);
        tvSignInUp.setVisibility(View.VISIBLE);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        progressBar = findViewById(R.id.progressBar);

        btnSignIn.setOnClickListener(v -> {
            if (haveNetwork()){
                studentSignIn();
            }else {
                Toast.makeText(this,getString(R.string.not_have_network),Toast.LENGTH_SHORT).show();
            }
        });

        btnSend.setOnClickListener(v -> {
            if (!validEmail()){
                return;
            }
            email = Objects.requireNonNull(tiEmail.getEditText()).getText().toString().trim();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, R.string.cekYourEmail, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }else {
                            Toast.makeText(SignInActivity.this, getText(R.string.notEmail), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        });

        tvSignInUp.setOnClickListener(v -> {
            startActivity(new Intent(this,SignUpActivity.class));
            overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
            finish();
        });

        tvForgotPassword.setOnClickListener(v -> {
            tvPassword.setVisibility(View.GONE);
            tiPassword.setVisibility(View.GONE);
            tvForgotPassword.setVisibility(View.INVISIBLE);
            tvSignInUp.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.INVISIBLE);
            btnSend.setVisibility(View.VISIBLE);
            tvForgotPassword.setVisibility(View.INVISIBLE);
            tvSignInUp.setVisibility(View.VISIBLE);
        });
    }
    private void studentSignIn(){
        if (!validEmail()|!validPassword()) {
            return;
        }
        email = Objects.requireNonNull(tiEmail.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(tiPassword.getEditText()).getText().toString().trim();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(getApplication(), HomeTabActivity.class));
                finish();
            }else {
                Toast.makeText(SignInActivity.this, getText(R.string.isWrong) + " " + getText(R.string.failed), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                tvForgotPassword.setVisibility(View.VISIBLE);
                tvSignInUp.setVisibility(View.INVISIBLE);
            }
        });
    }
    private boolean haveNetwork() {
        boolean haveConnection =false;
        ConnectivityManager connectivityManager = (ConnectivityManager) SignInActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
            haveConnection=true;
        }
        return haveConnection;
    }
    private boolean validEmail(){
        email = Objects.requireNonNull(tiEmail.getEditText()).getText().toString().trim();
        if (email.isEmpty()){
            tiEmail.setError(getText(R.string.notEmpty));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }else {
            tiEmail.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
    }
    private boolean validPassword(){
        password = Objects.requireNonNull(tiPassword.getEditText()).getText().toString().trim();
        if (password.isEmpty()){
            tiPassword.setError(getText(R.string.notEmpty));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }else {
            tiPassword.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
    }
}