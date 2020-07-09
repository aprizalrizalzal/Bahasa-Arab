package com.application.bahasa.arab.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.ui.main.DetailProfileActivity;
import com.application.bahasa.arab.ui.main.SectionsPagerAdapter;
import com.application.bahasa.arab.ui.main.chats.ListChatFragment;
import com.application.bahasa.arab.ui.main.chats.ListContactFragment;
import com.google.android.material.tabs.TabLayout;

public class ChatTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_chat);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        sectionsPagerAdapter.addFragment(new ListContactFragment(),getString(R.string.contact));
        sectionsPagerAdapter.addFragment(new ListChatFragment(),getString(R.string.chat));

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(R.string.app_name);
        }

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more,menu);
        MenuItem itemDownload = menu.findItem(R.id.menuDownloadAudio);
        itemDownload.setVisible(false);

        MenuItem itemSearch = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setQueryHint(getString(R.string.search_list));
        searchView.setOnSearchClickListener(v -> Toast.makeText(ChatTabActivity.this, getString(R.string.example),Toast.LENGTH_SHORT).show());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem itemAbout = menu.findItem(R.id.menuSetting);
        itemAbout.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(this, DetailProfileActivity.class));
            return false;
        });

        MenuItem itemSignOut = menu.findItem(R.id.menuSignOut);
        itemSignOut.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }
}