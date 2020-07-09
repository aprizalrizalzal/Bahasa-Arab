package com.application.bahasa.arab.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.DataModelProfile;
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

public class DetailProfileActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private StorageTask<UploadTask.TaskSnapshot> taskUpload;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Uri uriImage;
    private TextInputLayout tiStudentName,tiStudentIdNumber,tiPhoneNumber;
    private TextView edtStudentName,edtStudentIdNumber,edtPhoneNumber,edtEmail;
    private String studentName,studentIdNumber,phoneNumber;
    private ImageView imgPictureProfile;
    private static final int IMAGE_REQUEST = 1;
    private ImageButton btnEdit,btnSave;
    private ProgressBar progressBar;

    public DetailProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);

        storageReference = FirebaseStorage.getInstance().getReference(getString(R.string.valProfile));

        tiStudentName = findViewById(R.id.tiStudentNameProfile);
        tiStudentIdNumber = findViewById(R.id.tiStudentIdNumberProfile);
        tiPhoneNumber = findViewById(R.id.tiPhoneNumberProfile);
        imgPictureProfile = findViewById(R.id.imgPictureProfile);

        edtStudentName = findViewById(R.id.edtStudentNameProfile);
        edtStudentIdNumber = findViewById(R.id.edtStudentIdNumberProfile);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumberProfile);
        edtEmail = findViewById(R.id.edtEmailProfile);

        ImageButton btnAdd = findViewById(R.id.imgAddProfile);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        progressBar = findViewById(R.id.progressBar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(getString(R.string.user)).child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataModelProfile profile = snapshot.getValue(DataModelProfile.class);
                assert profile !=null;
                if (profile.getProfilePictureInTheURL().equals(getString(R.string.nothing))) {
                    imgPictureProfile.setImageResource(R.drawable.ic_baseline_account_circle);
                } else {
                    Glide.with(getApplicationContext())
                            .load(profile.getProfilePictureInTheURL())
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                            .error(R.drawable.ic_error)
                            .into(imgPictureProfile);
                }
                edtStudentName.setText(profile.getStudentName());
                edtStudentIdNumber.setText(profile.getStudentIdNumber());
                edtPhoneNumber.setText(profile.getPhoneNumber());
                edtEmail.setText(user.getEmail());
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
                edtStudentName.setEnabled(true);
                edtStudentIdNumber.setEnabled(true);
                edtPhoneNumber.setEnabled(true);
                btnSave.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.INVISIBLE);

            }else {
                Toast.makeText(this,getString(R.string.not_have_network),Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(v -> validProfile());

        AdView adViewUnit = findViewById(R.id.adViewProfile);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewUnit.loadAd(adRequest);
    }

    private boolean haveNetwork() {
        boolean haveConnection =false;
        ConnectivityManager connectivityManager = (ConnectivityManager) DetailProfileActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
            final StorageReference fileReference = storageReference.child(user.getUid() + "." + getFileExtension(uriImage));
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

                    reference = FirebaseDatabase.getInstance().getReference(getString(R.string.user)).child(user.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(getString(R.string.valProfilePictureInTheURL), myUri);
                    reference.updateChildren(map);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(DetailProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
        if (!validStudentName()|!validStudentIdNumber()|!validPhoneNumber()){
            return;
        }
        studentName = Objects.requireNonNull(tiStudentName.getEditText()).getText().toString().trim();
        studentIdNumber = Objects.requireNonNull(tiStudentIdNumber.getEditText()).getText().toString().trim();
        phoneNumber = Objects.requireNonNull(tiPhoneNumber.getEditText()).getText().toString().trim();

        reference=FirebaseDatabase.getInstance().getReference(getString(R.string.user)).child(user.getUid());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put(getString(R.string.valStudentName),studentName);
        hashMap.put(getString(R.string.valStudentIdNumber),studentIdNumber);
        hashMap.put(getString(R.string.valPhoneNumber),phoneNumber);

        reference.updateChildren(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(DetailProfileActivity.this, getString(R.string.saveSuccess), Toast.LENGTH_SHORT).show();
                btnSave.setVisibility(View.INVISIBLE);
                btnEdit.setVisibility(View.VISIBLE);
                edtStudentName.setEnabled(false);
                edtStudentIdNumber.setEnabled(false);
                edtPhoneNumber.setEnabled(false);
                progressBar.setVisibility(View.INVISIBLE);
            }else {
                Toast.makeText(DetailProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean validStudentName(){
        studentName = Objects.requireNonNull(tiStudentName.getEditText()).getText().toString().trim();
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

    private boolean validPhoneNumber(){
        phoneNumber = Objects.requireNonNull(tiPhoneNumber.getEditText()).getText().toString().trim();
        if (phoneNumber.isEmpty()){
            tiPhoneNumber.setError(getText(R.string.notEmptyStudentIdNumber));
            progressBar.setVisibility(View.INVISIBLE);
            return false;
        }else {
            tiPhoneNumber.setError(null);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }
    }
}