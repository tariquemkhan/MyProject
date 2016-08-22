package com.example.quickshare.activity;

import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.example.quickshare.R;
import com.example.quickshare.adapter.ViewPagerAdapter;
import com.example.quickshare.fragment.AppsFragment;
import com.example.quickshare.fragment.FileFragment;

public class FileTransferActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private ActionBar actionBar;

    private AppBarLayout appBarLayout;

    public static String TAG_NAME = "FileTransferActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_transfer);
        initComponent();
    }

    private void initComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_arrow_back_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FileFragment(), "Files");
        adapter.addFrag(new AppsFragment(), "Apps");
        adapter.addFrag(new FileFragment(), "Photos");
        adapter.addFrag(new FileFragment(), "Videos");
        adapter.addFrag(new FileFragment(), "Musics");
        adapter.addFrag(new FileFragment(), "History");
        viewPager.setAdapter(adapter);
    }
}
