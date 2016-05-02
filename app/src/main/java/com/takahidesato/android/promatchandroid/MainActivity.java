package com.takahidesato.android.promatchandroid;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public static final String YOUTUBE_API_KEY = "";
    public static final String TWITTER_API_KEY = "";
    public static final String TWITTER_API_SECRET = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("test", "coming herererererere");
        ViewPagerFragment fragment = (ViewPagerFragment) getSupportFragmentManager().findFragmentById(R.id.frg_view_pager);
        fragment.setCurrentItem(1);
    }
}
