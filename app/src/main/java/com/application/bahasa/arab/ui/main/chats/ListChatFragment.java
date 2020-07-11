package com.application.bahasa.arab.ui.main.chats;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.data.chats.DataModelListChat;
import com.application.bahasa.arab.data.chats.DataModelProfileOrContact;
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
import java.util.List;

public class ListChatFragment extends Fragment {

    private ListContactAdapter adapter;
    RecyclerView rv_chat;
    private List<DataModelProfileOrContact> profileOrContacts;

    private FirebaseUser user;
    private DatabaseReference reference;

    private List<DataModelListChat> listChats;

    public ListChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        listChats = new ArrayList<>();

        rv_chat = view.findViewById(R.id.rv_chat);
        rv_chat.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_chat.setHasFixedSize(true);

        AdView adViewChat = view.findViewById(R.id.adViewChat);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewChat.loadAd(adRequest);

        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listChats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataModelListChat chatList = snapshot.getValue(DataModelListChat.class);
                    listChats.add(chatList);
                }
                chatListUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void chatListUser() {
        profileOrContacts = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileOrContacts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataModelProfileOrContact user = snapshot.getValue(DataModelProfileOrContact.class);
                    for (DataModelListChat chatList : listChats) {
                        if (user != null && user.getId().equals(chatList.getId())) {
                            profileOrContacts.add(user);
                        }
                    }
                }
                adapter = new ListContactAdapter(getContext(), profileOrContacts, true);
                rv_chat.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}