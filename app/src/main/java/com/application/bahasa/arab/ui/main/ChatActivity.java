package com.application.bahasa.arab.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.DataModelChat;
import com.application.bahasa.arab.data.chats.DataModelProfileOrContact;
import com.application.bahasa.arab.ui.main.chats.ListMessageAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ImageView imageCoverContact;
    private TextView tv_item_listStudentName;
    private RecyclerView rv_chat;
    private EditText edtWriteMessage;
    private ImageButton btnSend;

    private List<DataModelChat> modelChats;
    private ListMessageAdapter adapter;
    private FirebaseUser user;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle(null);
        }

        AdView adViewMessage = findViewById(R.id.adViewMessage);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewMessage.loadAd(adRequest);

        imageCoverContact = findViewById(R.id.image_coverContactName);
        tv_item_listStudentName = findViewById(R.id.tv_list_studentName);
        rv_chat = findViewById(R.id.rv_chat);
        edtWriteMessage = findViewById(R.id.editTextMsg);
        btnSend = findViewById(R.id.btnSendMessage);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rv_chat.setHasFixedSize(true);
        rv_chat.setLayoutManager(linearLayoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();
        btnSend.setOnClickListener(v -> {
            String msg = edtWriteMessage.getText().toString();
            if (!msg.equals("")){
                sendMessage(user.getUid(),userId,msg);
            }else {
                Toast.makeText(ChatActivity.this, getString(R.string.notEmptyMessage),Toast.LENGTH_SHORT).show();
            }
            edtWriteMessage.setText("");
        });

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
                    if (profileOrContact != null) {
                        readMessage(user.getUid(),userId,profileOrContact.getProfilePictureInTheURL());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void sendMessage (String sender, final String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("readChats",false);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(user.getUid()).child(receiver);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessage (final String myId, final String userId, final String imageUrl){
        modelChats= new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelChats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataModelChat chats = snapshot.getValue(DataModelChat.class);
                    assert chats != null;
                    if (chats.getReceiver().equals(myId) && chats.getSender().equals(userId) ||
                            chats.getReceiver().equals(userId) && chats.getSender().equals(myId)){
                        modelChats.add(chats);
                    }
                    adapter = new ListMessageAdapter(ChatActivity.this,modelChats,imageUrl);
                    rv_chat.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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