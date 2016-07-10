package com.takahidesato.android.promatchandroid;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.takahidesato.android.promatchandroid.widget.FetchService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String YOUTUBE_API_KEY = BuildConfig.YOUTUBE_CONSUMER_KEY;
    public static final String TWITTER_API_KEY = BuildConfig.TWITTER_CONSUMER_KEY;
    public static final String TWITTER_API_SECRET = BuildConfig.TWITTER_CONSUMER_SECRET;

    public static final String FRAGMENT_KEY = "fragment_key";
    public static final int FRAGMENT_KEY_SUCCESS = 100;
    public static final int FRAGMENT_KEY_TWEETS = 200;
    public static final int FRAGMENT_KEY_SUCCESS_FAVORITE = 101;
    public static final int FRAGMENT_KEY_TWEETS_FAVORITE = 201;

    public static boolean IS_DUAL_PANE;

    private ActionBarDrawerToggle mDrawerToggle;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setDrawerToggle();
        setDrawerContent();
        setDetailFragment();

        if (findViewById(R.id.frl_view_pager) != null) {
            if (savedInstanceState != null) {
                return;
            }
            SuccessViewPagerFragment firstFragment = new SuccessViewPagerFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.frl_view_pager, firstFragment).commit();
        }

        Intent service_start = new Intent(this, FetchService.class);
        startService(service_start);
    }

    private void setDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

//                PageChannelsFragment fragment = (PageChannelsFragment) getFragmentManager().findFragmentByTag(PageChannelsFragment.class.getSimpleName());
//
//                if (fragment != null) {
//                    fragment.resetSearchFilter();
//                }
            }
        };
    }

    private void setDetailFragment() {
        View detailFragment = findViewById(R.id.frl_fragment_detail_container);
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
            manager.beginTransaction().replace(R.id.frl_fragment_detail_container, fragment, SuccessDetailFragment.TAG).commit();
        }
    }

    private void setDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_success:
                fragmentClass = SuccessViewPagerFragment.class;
                break;
            case R.id.nav_tweets:
                fragmentClass = TweetsViewPagerFragment.class;
                break;
            case R.id.nav_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return;
            default:
                fragmentClass = SuccessViewPagerFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frl_view_pager, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
//            case R.id.action_about:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
