package com.application.bahasa.arab.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class HomeTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(R.string.app_name);
        }

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, view.getResources().getString(R.string.example), Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setTextColor(getResources().getColor(R.color.browser_actions_text_color))
                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more,menu);
        MenuItem itemDownload = menu.findItem(R.id.menuDownloadAudio);
        itemDownload.setVisible(false);
        MenuItem itemAbout = menu.findItem(R.id.menuAbout);
        itemAbout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(HomeTabActivity.this, getString(R.string.example),Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}