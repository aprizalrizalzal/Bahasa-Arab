package com.application.bahasa.arab.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.ui.additional.ListAdditionalFragment;
import com.application.bahasa.arab.ui.main.DetailProfileActivity;
import com.application.bahasa.arab.ui.main.SectionsPagerAdapter;
import com.application.bahasa.arab.ui.semester.ListSemesterFragment;
import com.application.bahasa.arab.ui.sign.SignInActivity;
import com.application.bahasa.arab.ui.unit.ListUnitFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        sectionsPagerAdapter.addFragment(new ListSemesterFragment(),getString(R.string.semester));
        sectionsPagerAdapter.addFragment(new ListUnitFragment(),getString(R.string.theUnit));
        sectionsPagerAdapter.addFragment(new ListAdditionalFragment(),getString(R.string.additional));

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
        fab.setOnClickListener(view -> {
            if (haveNetwork()){
                startActivity(new Intent(this, ChatTabActivity.class));
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
            }else {
                Snackbar.make(view,getString( R.string.not_have_network), Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setTextColor(view.getResources().getColor(R.color.browser_actions_text_color))
                        .setBackgroundTint(view.getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more,menu);
        MenuItem itemDownload = menu.findItem(R.id.menuDownloadAudio);
        itemDownload.setVisible(false);

        MenuItem itemSearch = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setQueryHint(getString(R.string.search_list));
        searchView.setOnSearchClickListener(v -> Toast.makeText(HomeTabActivity.this, getString(R.string.example),Toast.LENGTH_SHORT).show());
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
        itemSignOut.setOnMenuItemClickListener(item -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(HomeTabActivity.this,getText(R.string.signOut) ,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeTabActivity.this, SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private boolean haveNetwork() {
        boolean haveConnection =false;
        ConnectivityManager connectivityManager = (ConnectivityManager) HomeTabActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo !=null && activeNetworkInfo.isConnected()){
            haveConnection=true;
        }
        return haveConnection;
    }
}