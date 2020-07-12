package com.application.bahasa.arab.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.ModelContactList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private StorageTask<UploadTask.TaskSnapshot> taskUpload;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Uri uriImage;
    private TextInputLayout tiUserName,tiPhoneNumber;
    private TextView edtUserName,edtPhoneNumber,edtEmail;
    private String userName,phoneNumber;
    private ImageView imgCoverProfile;
    private static final int IMAGE_REQUEST = 1;
    private Button btnEdit,btnSave;
    private ProgressBar progressBar;

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle(getString(R.string.profile));
        }

        AdView adViewProfile = findViewById(R.id.adViewProfile);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewProfile.loadAd(adRequest);

        storageReference = FirebaseStorage.getInstance().getReference("profile/user");

        tiUserName = findViewById(R.id.tiUserName);
        tiPhoneNumber = findViewById(R.id.tiPhoneNumber);
        imgCoverProfile = findViewById(R.id.imgCoverProfile);

        edtUserName = findViewById(R.id.edtUserName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtEmail = findViewById(R.id.edtEmail);

        ImageButton btnAdd = findViewById(R.id.imgAddProfile);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        progressBar = findViewById(R.id.progressBar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelContactList profile = snapshot.getValue(ModelContactList.class);
                assert profile !=null;
                if (profile.getProfilePicture().equals("nothing")) {
                    imgCoverProfile.setImageResource(R.drawable.ic_baseline_account_circle);
                } else {
                    Glide.with(getApplicationContext())
                            .load(profile.getProfilePicture())
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                            .error(R.drawable.ic_error)
                            .into(imgCoverProfile);
                }
                edtUserName.setText(profile.getUserName());
                edtPhoneNumber.setText(profile.getPhoneNumber());
                edtEmail.setText(firebaseUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAdd.setOnClickListener(v -> {
            if (haveNetwork()){
                openImage();
            }else {
                Toast.makeText(this,getString(R.string.not_have_network),Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> {
            if (haveNetwork()){
                edtUserName.setEnabled(true);
                edtPhoneNumber.setEnabled(true);
                btnSave.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.INVISIBLE);

            }else {
                Toast.makeText(this,getString(R.string.not_have_network),Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(v -> validProfile());
    }

    private boolean haveNetwork() {
        boolean haveConnection =false;
        ConnectivityManager connectivityManager = (ConnectivityManager) ProfileActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
            haveConnection=true;
        }
        return haveConnection;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        progressBar.setVisibility(View.VISIBLE);
        if (uriImage != null) {
            final StorageReference fileReference = storageReference.child(firebaseUser.getUid() + "." + getFileExtension(uriImage));
            taskUpload = fileReference.putFile(uriImage);
            taskUpload.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    String myUri = downloadUri.toString();

                    databaseReference = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
                    HashMap<String, Object> user = new HashMap<>();
                    user.put("profilePicture", myUri);
                    databaseReference.updateChildren(user);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();

            if (taskUpload != null && taskUpload.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Upload In Progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    private void validProfile(){
        if (!validStudentName()|!validPhoneNumber()){
            return;
        }
        userName = Objects.requireNonNull(tiUserName.getEditText()).getText().toString().trim();
        phoneNumber = Objects.requireNonNull(tiPhoneNumber.getEditText()).getText().toString().trim();

        databaseReference=FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userName",userName);
        hashMap.put("phoneNumber",phoneNumber);

        databaseReference.updateChildren(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfileActivity.this, getString(R.string.saveSuccess), Toast.LENGTH_SHORT).show();
                btnSave.setVisibility(View.INVISIBLE);
                btnEdit.setVisibility(View.VISIBLE);
                edtUserName.setEnabled(false);
                edtPhoneNumber.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
            }else {
                Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean validStudentName(){
        userName = Objects.requireNonNull(tiUserName.getEditText()).getText().toString().trim();
        if (userName.isEmpty()){
            tiUserName.setError(getText(R.string.notEmpty));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }else {
            tiUserName.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private boolean validPhoneNumber(){
        phoneNumber = Objects.requireNonNull(tiPhoneNumber.getEditText()).getText().toString().trim();
        if (phoneNumber.isEmpty()){
            tiPhoneNumber.setError(getText(R.string.notEmpty));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }else {
            tiPhoneNumber.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
    }
}