package com.application.bahasa.arab.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.application.bahasa.arab.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_OVERVIEW = "extra_overview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        PDFView pdfView = (PDFView) findViewById(R.id.pdfViewUnit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String detailOverview = extras.getString(EXTRA_OVERVIEW);
            if (detailOverview != null) {
                pdfView.fromAsset(detailOverview)
                        .pageSnap(true)
                        .swipeHorizontal(true)
                        .enableSwipe(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .spacing(10)
                        .load();
            }
        }
    }
}