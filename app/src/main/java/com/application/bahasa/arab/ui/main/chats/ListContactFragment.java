package com.application.bahasa.arab.ui.main.chats;

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

public class ListContactFragment extends Fragment {

    private List<DataModelProfileOrContact> profileOrContacts = new ArrayList<>();
    private ListContactAdapter adapter;
    private RecyclerView rvContact;

    public ListContactFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvContact = view.findViewById(R.id.rv_contact);
        AdView adViewContact = view.findViewById(R.id.adViewContact);

        if (getActivity() !=null){
            readContact();
            AdRequest adRequest = new AdRequest.Builder().build();
            adViewContact.loadAd(adRequest);
        }
    }

    private void readContact() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileOrContacts.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DataModelProfileOrContact profileOrContact = dataSnapshot.getValue(DataModelProfileOrContact.class);
                    if (user != null && profileOrContact != null && !profileOrContact.getId().equals(user.getUid())) {
                        profileOrContacts.add(profileOrContact);
                    }
                }
                adapter = new ListContactAdapter(getContext(), profileOrContacts, false);

                rvContact.setLayoutManager(new LinearLayoutManager(getContext()));
                rvContact.setHasFixedSize(true);
                rvContact.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}