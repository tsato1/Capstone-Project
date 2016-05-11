package com.takahidesato.android.promatchandroid;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    public static final String YOUTUBE_API_KEY = "";
    public static final String TWITTER_API_KEY = "";
    public static final String TWITTER_API_SECRET = "";

    public static boolean IS_DUAL_PANE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IS_DUAL_PANE = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (IS_DUAL_PANE) {
            Fragment fragment = null;
            Class fragmentClass = SuccessStoriesDetailFragment.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            fragment.setArguments(new Bundle());

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frl_fragment_container, fragment, SuccessStoriesDetailFragment.TAG).commit();
        }

    }
}
