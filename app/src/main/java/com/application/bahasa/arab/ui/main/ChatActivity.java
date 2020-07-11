package com.application.bahasa.arab.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.DataModelProfileOrContact;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private ImageView imageCoverContact;
    private TextView tv_item_listStudentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle(null);
        }

        imageCoverContact = findViewById(R.id.image_coverContactName);
        tv_item_listStudentName = findViewById(R.id.tv_list_studentName);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        if (userId != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(userId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DataModelProfileOrContact profileOrContact = snapshot.getValue(DataModelProfileOrContact.class);
                    if (profileOrContact != null) {
                        tv_item_listStudentName.setText(profileOrContact.getStudentName());
                        if (profileOrContact.getProfilePictureInTheURL().equals("nothing")){
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.ic_baseline_account_circle)
                                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                                    .error(R.drawable.ic_error)
                                    .into(imageCoverContact);
                        }else {
                            Glide.with(getApplicationContext())
                                    .load(profileOrContact.getProfilePictureInTheURL())
                                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                                    .error(R.drawable.ic_error)
                                    .into(imageCoverContact);}
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more,menu);
        MenuItem itemDownload = menu.findItem(R.id.menuDownloadAudio);
        itemDownload.setVisible(false);

        MenuItem itemSearch = menu.findItem(R.id.searchView);
        itemSearch.setVisible(false);

        MenuItem menuSetting = menu.findItem(R.id.menuSetting);
        menuSetting.setOnMenuItemClickListener(v -> {
            Toast.makeText(ChatActivity.this, getString(R.string.example),Toast.LENGTH_SHORT).show();
            return false;
        });

        MenuItem itemProfile = menu.findItem(R.id.menuProfile);
        itemProfile.setVisible(false);

        MenuItem itemSignOut = menu.findItem(R.id.menuSignOut);
        itemSignOut.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }
}