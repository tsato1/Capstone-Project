package com.takahidesato.android.promatchandroid;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.takahidesato.android.promatchandroid.widget.FetchService;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String YOUTUBE_API_KEY = BuildConfig.YOUTUBE_CONSUMER_KEY;
    public static final String TWITTER_API_KEY = BuildConfig.TWITTER_CONSUMER_KEY;
    public static final String TWITTER_API_SECRET = BuildConfig.TWITTER_CONSUMER_SECRET;

    public static boolean IS_DUAL_PANE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View detailFragment = findViewById(R.id.frl_fragment_container);
        IS_DUAL_PANE = detailFragment != null && detailFragment.getVisibility() == View.VISIBLE;

        if (IS_DUAL_PANE) {
            Fragment fragment = null;
            Class fragmentClass = SuccessDetailFragment.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            fragment.setArguments(new Bundle());

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.frl_fragment_container, fragment, SuccessDetailFragment.TAG).commit();
        }

        Intent service_start = new Intent(this, FetchService.class);
        startService(service_start);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
