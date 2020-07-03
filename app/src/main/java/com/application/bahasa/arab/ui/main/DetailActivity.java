package com.application.bahasa.arab.ui.main;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.application.bahasa.arab.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.List;

public class DetailActivity extends AppCompatActivity{

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_LINK_MP3 = "extra_link_mp3";

    private MediaPlayer mediaPlayer;

    private ImageButton imageFirst, imagePlay, imagePause, imageLast, imageStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        AdView adViewUnit = findViewById(R.id.adViewDetail);

        imageFirst = findViewById(R.id.btnFirst);
        imagePlay = findViewById(R.id.btnPlay);
        imagePause = findViewById(R.id.btnPause);
        imageLast = findViewById(R.id.btnLast);
        imageStop = findViewById(R.id.btnStop);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adViewUnit.loadAd(adRequest);

        PDFView pdfView = findViewById(R.id.pdfViewDetail);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        Bundle document = getIntent().getExtras();
        if (document != null) {
            String detailTitle = document.getString(EXTRA_TITLE);
            File path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (detailTitle != null) {
                progressBar.setVisibility(View.INVISIBLE);

                pdfView.fromFile(new File(path, detailTitle))
                        .pageSnap(true)
                        .swipeHorizontal(true)
                        .enableSwipe(true)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .spacing(10)
                        .load();
                imageFirst.setOnClickListener(v -> {
                    pdfView.jumpTo(pdfView.getCurrentPage() -1, true);
                    Toast.makeText(this,getString(R.string.first), Toast.LENGTH_SHORT).show();

                });
                imageLast.setOnClickListener(v -> {
                    pdfView.jumpTo(pdfView.getCurrentPage() +1, true);
                    Toast.makeText(this,getString(R.string.last), Toast.LENGTH_SHORT).show();

                });
            }
        }
    }

    private boolean haveNetwork() {
        boolean haveConnection =false;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
            haveConnection=true;
        }
        return haveConnection;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more,menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem menuDownloadAudio = menu.findItem(R.id.menuDownloadAudio);

        Bundle fileExist = getIntent().getExtras();
        if (fileExist !=null){
            String audioTitle = fileExist.getString(EXTRA_TITLE);
            if (audioTitle !=null){
                File file = new File(String.valueOf(this.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)),audioTitle);
                if (file.exists()){
                    menuDownloadAudio.setVisible(false);
                    imagePlay.setVisibility(View.VISIBLE);
                }

                mediaPlayer = MediaPlayer.create(this, Uri.fromFile(file));

                imagePlay.setOnClickListener(v ->{
                    imagePlay.setVisibility(View.INVISIBLE);
                    if (mediaPlayer !=null){
                        mediaPlayer.start();
                        imagePause.setVisibility(View.VISIBLE);
                        imageStop.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(this,"Silahkan Keluar dan Kembali Lagi", Toast.LENGTH_SHORT).show();
                    }
                });

                imagePause.setOnClickListener(v ->{
                    imagePlay.setVisibility(View.VISIBLE);
                    mediaPlayer.pause();
                    imagePause.setVisibility(View.INVISIBLE);
                });

                imageStop.setOnClickListener(v ->{
                    imagePlay.setVisibility(View.VISIBLE);
                    mediaPlayer.stop();
                    imagePause.setVisibility(View.INVISIBLE);
                });

            }
        }

        menuDownloadAudio.setOnMenuItemClickListener(menuItem -> {
            menuDownloadAudio.setVisible(false);

            Bundle audio = getIntent().getExtras();
            if (audio !=null){
                String detailAudio = audio.getString(EXTRA_LINK_MP3);
                String detailTitle = audio.getString(EXTRA_TITLE);
                if (detailAudio !=null){
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(detailAudio));
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PODCASTS,detailTitle);

                    DownloadManager downloadManager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
                    request.allowScanningByMediaScanner();
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                    if (haveNetwork()){
                        PermissionListener permissionListener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                downloadManager.enqueue(request);
                                menuDownloadAudio.setVisible(false);
                                imagePlay.setVisibility(View.VISIBLE);

                            }
                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                menuDownloadAudio.setVisible(true);

                            }
                        };
                        TedPermission.with(this)
                                .setPermissionListener(permissionListener)
                                .setDeniedMessage(R.string.denied_message)
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .check();

                    }else if (!haveNetwork()){
                        Toast.makeText(this,R.string.not_have_network, Toast.LENGTH_SHORT).show();
                        menuDownloadAudio.setVisible(true);
                    }
                }
            }
            return true;
        });

        MenuItem menuSearch = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuSearch.getActionView();
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