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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.ui.HomeTabActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView tvEmail,tvPassword;
    private TextInputLayout tiEmail,tiPassword;
    private String email,password;
    private Button btnSignUp,btnSignIn;
    private TextView tvTermsAndConditions,tvSignInUp;
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

        TextView tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentName.setVisibility(View.INVISIBLE);
        TextView tvStudentIdNumber = findViewById(R.id.tvStudentIdNumber);
        tvStudentIdNumber.setVisibility(View.GONE);
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        TextView tvConfirmPassword = findViewById(R.id.tvConfirmPassword);
        tvConfirmPassword.setVisibility(View.GONE);

        TextInputLayout tiStudentName = findViewById(R.id.tiStudentName);
        tiStudentName.setVisibility(View.GONE);
        TextInputLayout tiStudentIdNumber = findViewById(R.id.tiStudentIdNumber);
        tiStudentIdNumber.setVisibility(View.GONE);
        tiEmail = findViewById(R.id.tiEmail);
        tiPassword = findViewById(R.id.tiPassword);
        TextInputLayout tiConfirmPassword = findViewById(R.id.tiConfirmPassword);
        tiConfirmPassword.setVisibility(View.GONE);


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

        progressBar = findViewById(R.id.progressBar);

        btnSignIn.setOnClickListener(v -> {
            if (haveNetwork()){
                studentSignIn();
            }else {
                Snackbar.make(btnSignUp, "Maaf, Tidak Ada Koneksi Internet ", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                        .setAction("Action", null)
                        .show();
            }
        });

        tvSignInUp.setOnClickListener(v -> {
            startActivity(new Intent(this,SignUpActivity.class));
            finish();
        });
    }
    private void studentSignIn(){
        if (!validEmail()|!validPassword()) {
            return;
        }
        email = Objects.requireNonNull(tiEmail.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(tiPassword.getEditText()).getText().toString().trim();

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplication(), HomeTabActivity.class));
                    finish();
                }else {
                    Toast.makeText(SignInActivity.this, getText(R.string.isWrong) + " " + getText(R.string.failed), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
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
            tiEmail.setError(getText(R.string.notEmptyEmail));
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
            tiPassword.setError(getText(R.string.notEmptyPassword));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }else {
            tiPassword.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
    }
}