package com.application.bahasa.arab.ui.contact;

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
import com.application.bahasa.arab.data.chats.ModelContactList;
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

    private List<ModelContactList> modelContactList = new ArrayList<>();
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
        rvContact = view.findViewById(R.id.rvContact);
        AdView adViewContact = view.findViewById(R.id.adViewContact);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewContact.loadAd(adRequest);

        if (getActivity() !=null){
            readContact();
        }
    }

    private void readContact() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelContactList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ModelContactList modelContact = dataSnapshot.getValue(ModelContactList.class);
                    if (user != null && modelContact != null && !modelContact.getUserId().equals(user.getUid())) {
                        modelContactList.add(modelContact);
                    }
                }
                adapter = new ListContactAdapter(getContext(), modelContactList, false);
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