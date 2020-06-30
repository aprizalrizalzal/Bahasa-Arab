package com.application.bahasa.arab.ui.main;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.application.bahasa.arab.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;

public class DetailActivity extends AppCompatActivity{

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_OVERVIEW = "extra_overview";
    public static final String EXTRA_LINK = "extra_link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        AdView adViewUnit = findViewById(R.id.adViewDetail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        MobileAds.initialize(this,"ca-app-pub-3946611117968254~4667483765");
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewUnit.loadAd(adRequest);

        PDFView pdfView = findViewById(R.id.pdfViewDetail);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String detailTitle = extras.getString(EXTRA_TITLE);
            File path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (detailTitle != null) {
                progressBar.setVisibility(View.INVISIBLE);
                pdfView.fromFile(new File(path,detailTitle))
                        .pageSnap(true)
                        .swipeHorizontal(true)
                        .enableSwipe(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .spacing(10)
                        .load();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }
}