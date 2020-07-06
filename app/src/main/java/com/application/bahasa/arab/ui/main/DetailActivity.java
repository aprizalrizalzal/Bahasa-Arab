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
    private ImageButton imagePlay;
    private ImageButton imagePause;
    private ImageButton imageStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        AdView adViewUnit = findViewById(R.id.adViewDetail);

        ImageButton imageFirst = findViewById(R.id.btnFirst);
        imagePlay = findViewById(R.id.btnPlay);
        imagePause = findViewById(R.id.btnPause);
        ImageButton imageLast = findViewById(R.id.btnLast);
        imageStop = findViewById(R.id.btnStop);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() !=null) {
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
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        imagePlay.setOnClickListener(v ->{
            playMedia();
        });

        imagePause.setOnClickListener(v ->{
            pauseMedia();
        });

        imageStop.setOnClickListener(v ->{
            stopMedia();
        });

    }

    private void playMedia(){
        if (mediaPlayer != null){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopMedia();
                }
            });
            imagePlay.setVisibility(View.INVISIBLE);
            imagePause.setVisibility(View.VISIBLE);
            imageStop.setVisibility(View.VISIBLE);
        }else {
            Bundle audio = getIntent().getExtras();
            if (audio !=null){
                String audioTitle = audio.getString(EXTRA_TITLE);
                if (audioTitle !=null) {
                    File file = getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
                    mediaPlayer = MediaPlayer.create(this, Uri.fromFile(new File(file, audioTitle)));
                    if (mediaPlayer == null){
                        Toast.makeText(DetailActivity.this, getString(R.string.media_is_empty),Toast.LENGTH_SHORT).show();
                    }else {
                        mediaPlayer.start();
                        imagePlay.setVisibility(View.INVISIBLE);
                        imagePause.setVisibility(View.VISIBLE);
                        imageStop.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void pauseMedia(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
            imagePlay.setVisibility(View.VISIBLE);
            imagePause.setVisibility(View.INVISIBLE);
        }

    }

    private void stopMedia(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            imageStop.setVisibility(View.INVISIBLE);
            imagePause.setVisibility(View.INVISIBLE);
            imagePlay.setVisibility(View.VISIBLE);
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
        MenuItem menuDownloadAudio = menu.findItem(R.id.menuDownloadAudio);
        MenuItem menuAbout = menu.findItem(R.id.menuAbout);
        menuAbout.setOnMenuItemClickListener(v -> {
            Toast.makeText(DetailActivity.this, getString(R.string.example),Toast.LENGTH_SHORT).show();
            return true;
        });

        menuDownloadAudio.setOnMenuItemClickListener(v -> {
            Toast.makeText(DetailActivity.this, getString(R.string.example),Toast.LENGTH_SHORT).show();
            menuDownloadAudio.setVisible(false);

            /*Bundle audio = getIntent().getExtras();
            if (audio !=null){
                String detailAudio = audio.getString(EXTRA_LINK_MP3);
                String detailTitle = audio.getString(EXTRA_TITLE);
                if (detailAudio !=null){
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(detailAudio));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PODCASTS,detailTitle);

                    DownloadManager downloadManager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

                    if (haveNetwork()){
                        PermissionListener permissionListener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                *//*downloadManager.enqueue(request);
                                Toast.makeText(DetailActivity.this, getString(R.string.download_audio) +" "+ detailTitle,Toast.LENGTH_SHORT).show();*//*
                            }
                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                menuDownloadAudio.setVisible(true);
                                Toast.makeText(DetailActivity.this, getString(R.string.denied_permission),Toast.LENGTH_SHORT).show();
                            }
                        };
                        TedPermission.with(this)
                                .setPermissionListener(permissionListener)
                                .setDeniedMessage(R.string.denied_message)
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .check();

                    }else if (!haveNetwork()){
                        Toast.makeText(DetailActivity.this, getString(R.string.not_have_network),Toast.LENGTH_SHORT).show();
                        menuDownloadAudio.setVisible(true);
                    }
                }
            }*/
            return true;
        });
        return super.onCreateOptionsMenu(menu);
    }
}