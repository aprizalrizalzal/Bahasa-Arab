package com.application.bahasa.arab.ui.chats;

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
import com.application.bahasa.arab.data.chats.ModelChatList;
import com.application.bahasa.arab.data.chats.ModelContactList;
import com.application.bahasa.arab.ui.contact.ListContactAdapter;
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
    private List<ModelContactList> modelContactLists;
    private List<ModelChatList> modelChatList;
    private RecyclerView rvChat;

    private DatabaseReference databaseReference;


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
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        modelChatList = new ArrayList<>();

        rvChat = view.findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setHasFixedSize(true);

        AdView adViewChat = view.findViewById(R.id.adViewChat);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewChat.loadAd(adRequest);

        if (firebaseUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("chatList").child(firebaseUser.getUid());
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelChatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelChatList chatList = snapshot.getValue(ModelChatList.class);
                    modelChatList.add(chatList);
                }
                chatListUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void chatListUser() {
        modelContactLists = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelContactLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelContactList user = snapshot.getValue(ModelContactList.class);
                    for (ModelChatList chatList : modelChatList) {
                        if (user != null && user.getUserId().equals(chatList.getUserId())) {
                            modelContactLists.add(user);
                        }
                    }
                }
                adapter = new ListContactAdapter(getContext(), modelContactLists, true);
                rvChat.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}