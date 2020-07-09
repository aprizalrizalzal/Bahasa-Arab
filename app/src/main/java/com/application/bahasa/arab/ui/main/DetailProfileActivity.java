package com.application.bahasa.arab.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.application.bahasa.arab.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DetailProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);



        AdView adViewUnit = findViewById(R.id.adViewProfile);
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewUnit.loadAd(adRequest);
    }
}