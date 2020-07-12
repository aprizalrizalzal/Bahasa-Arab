package com.application.bahasa.arab.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.ModelChat;
import com.application.bahasa.arab.data.chats.ModelContactList;
import com.application.bahasa.arab.ui.chats.ListChatAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private ImageView imageCoverContact;
    private TextView tvUserName;
    private RecyclerView rvMessage;
    private EditText edtMessage;

    private List<ModelChat> modelChat;
    private ListChatAdapter adapter;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle(null);
        }

        AdView adViewMessage = findViewById(R.id.adViewMessage);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewMessage.loadAd(adRequest);

        imageCoverContact = findViewById(R.id.imageCoverContact);
        tvUserName = findViewById(R.id.tvUserName);
        rvMessage = findViewById(R.id.rvMessage);
        edtMessage = findViewById(R.id.edtMessage);
        ImageButton btnSendMessage = findViewById(R.id.btnSendMessage);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvMessage.setHasFixedSize(true);
        rvMessage.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btnSendMessage.setOnClickListener(v -> {
            String message = edtMessage.getText().toString();
            if (!message.equals("")){
                sendMessage(firebaseUser.getUid(),userId,message);
            }else {
                Toast.makeText(MessageActivity.this, getString(R.string.notEmptyMessage),Toast.LENGTH_SHORT).show();
            }
            edtMessage.setText("");
        });

        if (userId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ModelContactList modelContactList = snapshot.getValue(ModelContactList.class);
                    if (modelContactList != null) {
                        tvUserName.setText(modelContactList.getUserName());
                        if (modelContactList.getProfilePicture().equals("nothing")){
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.ic_baseline_account_circle)
                                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                                    .error(R.drawable.ic_error)
                                    .into(imageCoverContact);
                        }else {
                            Glide.with(getApplicationContext())
                                    .load(modelContactList.getProfilePicture())
                                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                                    .error(R.drawable.ic_error)
                                    .into(imageCoverContact);}
                    }
                    if (modelContactList != null) {
                        readMessage(firebaseUser.getUid(),userId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            readChat(userId);
        }
    }

    private void readChat(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelChat chat = snapshot.getValue(ModelChat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)) {
                        HashMap<String, Object> user = new HashMap<>();
                        user.put("readChats", true);
                        snapshot.getRef().updateChildren(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage (String sender, final String receiver, String message){

        databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> user = new HashMap<>();
        user.put("sender",sender);
        user.put("receiver",receiver);
        user.put("message",message);
        user.put("readChats",false);

        databaseReference.child("chat").push().setValue(user);

        databaseReference = FirebaseDatabase.getInstance().getReference("chatList").child(firebaseUser.getUid()).child(receiver);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    databaseReference.child("userId").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessage (final String myId, final String userId){
        modelChat= new ArrayList<>();

        databaseReference=FirebaseDatabase.getInstance().getReference("chat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelChat chat = snapshot.getValue(ModelChat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                        modelChat.add(chat);
                    }
                    adapter = new ListChatAdapter(MessageActivity.this,modelChat);
                    rvMessage.setAdapter(adapter);
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
            Toast.makeText(MessageActivity.this, getString(R.string.example),Toast.LENGTH_SHORT).show();
            return false;
        });

        MenuItem itemProfile = menu.findItem(R.id.menuProfile);
        itemProfile.setVisible(false);

        MenuItem itemSignOut = menu.findItem(R.id.menuSignOut);
        itemSignOut.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }
}