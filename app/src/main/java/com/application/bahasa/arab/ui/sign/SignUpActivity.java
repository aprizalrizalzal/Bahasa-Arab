package com.application.bahasa.arab.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.application.bahasa.arab.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private TextInputLayout tiStudentName,tiStudentIdNumber,tiEmail,tiPassword,tiConfirmPassword;
    private String studentName,studentIdNumber,email,password;
    private Button btnSignUp;
    private CheckBox checkBox;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_in);

        AdView adViewUnit = findViewById(R.id.adViewSign);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewUnit.loadAd(adRequest);

        auth=FirebaseAuth.getInstance();

        tiStudentName = findViewById(R.id.tiStudentName);
        tiStudentIdNumber = findViewById(R.id.tiStudentIdNumber);
        tiEmail = findViewById(R.id.tiEmail);
        tiPassword = findViewById(R.id.tiPassword);
        tiConfirmPassword = findViewById(R.id.tiConfirmPassword);

        btnSignUp = findViewById(R.id.btnSignUp);

        checkBox = findViewById(R.id.checkBok);

        TextView tvTermsAndConditions = findViewById(R.id.tvTermsAndConditions);
        TextView tvSignUpIn = findViewById(R.id.tvSignUpIn);

        progressBar = findViewById(R.id.progressBar);

        btnSignUp.setOnClickListener(v -> {
            if (haveNetwork()){
                studentSignUp();
            }else {
                Toast.makeText(this,getString(R.string.not_have_network),Toast.LENGTH_SHORT).show();
            }
        });

        btnSignUp.setEnabled(false);
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()){
                btnSignUp.setEnabled(true);
            }else {
                btnSignUp.setEnabled(false);
            }
        });

        tvTermsAndConditions.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://unu-ntb.ac.id/"))));

        tvSignUpIn.setOnClickListener(v -> {
            startActivity(new Intent(this,SignInActivity.class));
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
            finish();
        });
    }

    private void studentSignUp(){
        if (!validStudentName()|!validStudentIdNumber()|!validEmail()|!validPassword()|!validConfirmPassword()) {
            return;
        }
        studentName = Objects.requireNonNull(tiStudentName.getEditText()).getText().toString().trim();
        studentIdNumber = Objects.requireNonNull(tiStudentIdNumber.getEditText()).getText().toString().trim();
        email = Objects.requireNonNull(tiEmail.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(tiPassword.getEditText()).getText().toString().trim();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(taskCreate -> {
            if (taskCreate.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();
                assert user != null;
                String userId = user.getUid();
                reference= FirebaseDatabase.getInstance().getReference("User").child(userId);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id",userId);
                hashMap.put("studentName",studentName);
                hashMap.put("studentIdNumber",studentIdNumber);
                hashMap.put("phoneNumber","nothing");
                hashMap.put("profilePictureInTheURL","nothing");

                reference.setValue(hashMap).addOnCompleteListener(taskReference -> {
                    if (taskReference.isSuccessful()){
                        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                        finish();
                    }
                });

            }else {
                Toast.makeText(SignUpActivity.this,getText(R.string.failed),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean haveNetwork() {
        boolean haveConnection =false;
        ConnectivityManager connectivityManager = (ConnectivityManager) SignUpActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
            haveConnection=true;
        }
        return haveConnection;
    }

    private boolean validStudentName(){
        String studentName = Objects.requireNonNull(tiStudentName.getEditText()).getText().toString().trim();
        if (studentName.isEmpty()){
            tiStudentName.setError(getText(R.string.notEmptyStudentName));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }else {
            tiStudentName.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private boolean validStudentIdNumber(){
        studentIdNumber = Objects.requireNonNull(tiStudentIdNumber.getEditText()).getText().toString().trim();
        if (studentIdNumber.isEmpty()){
            tiStudentIdNumber.setError(getText(R.string.notEmptyStudentIdNumber));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }else {
            tiStudentIdNumber.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
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
    private boolean validConfirmPassword(){
        String confirmPassword = Objects.requireNonNull(tiConfirmPassword.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(tiPassword.getEditText()).getText().toString().trim();
        if (confirmPassword.isEmpty()){
            tiConfirmPassword.setError(getText(R.string.notEmptyConfirmPassword));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
        else if (!confirmPassword.equals(password)){
            tiConfirmPassword.setError(getText(R.string.notEqualsConfirmPassword));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }
        else {
            tiConfirmPassword.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
    }

}