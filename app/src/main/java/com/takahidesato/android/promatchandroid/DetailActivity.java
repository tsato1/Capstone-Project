package com.takahidesato.android.promatchandroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by tsato on 4/15/16.
 */
public class DetailActivity extends AppCompatActivity {
    private int key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if ((screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            finish();
            return;
        }

        key = getIntent().getExtras().getInt(MainActivity.FRAGMENT_KEY);

        Fragment fragment = null;
        Class fragmentClass = SuccessDetailFragment.class;
        switch(key) {
            case MainActivity.FRAGMENT_KEY_SUCCESS:
                fragmentClass = SuccessDetailFragment.class;
                break;
            case MainActivity.FRAGMENT_KEY_TWEETS:
                fragmentClass = TweetsDetailFragment.class;
                break;
            case MainActivity.FRAGMENT_KEY_SUCCESS_FAVORITE:
                fragmentClass = SuccessDetailFragment.class;
                break;
            case MainActivity.FRAGMENT_KEY_TWEETS_FAVORITE:
                fragmentClass = TweetsDetailFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(MainActivity.FRAGMENT_KEY, key);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        key = savedInstanceState.getInt(MainActivity.FRAGMENT_KEY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
