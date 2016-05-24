package com.takahidesato.android.promatchandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.takahidesato.android.promatchandroid.tab.SlidingTabLayout;

/**
 * Created by tsato on 4/15/16.
 */
public class ViewPagerFragment extends Fragment {
    private enum fragment {
        SUCCESS,
        TWEETS,
        SUCCESS_FAVORITE,
        TWEETS_FAVORITE
    }
    public static final String FRAGMENT_KEY = "fragment_key";
    public static final int FRAGMENT_KEY_SUCCESS = 0;
    public static final int FRAGMENT_KEY_TWEETS = 1;
    public static final int FRAGMENT_KEY_SUCCESS_FAVORITE = 2;
    public static final int FRAGMENT_KEY_TWEETS_FAVORITE = 3;

    private static final String TAG = ViewPagerFragment.class.getSimpleName();
    private static final int NUM_ITEMS = 4;

    private Context mContext;

    private ViewPagerAdapter mAdatper;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();

        mAdatper = new ViewPagerAdapter(getChildFragmentManager());

        mPager = (ViewPager) getView().findViewById(R.id.view_pager);
        mPager.setAdapter(mAdatper);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (MainActivity.IS_DUAL_PANE) {
                    prepareDetailFragment(position);
                }

                Log.i(TAG, "Setting screen name: " + fragment.values()[position]);
                mTracker.setScreenName("Screen Name = " + fragment.values()[position]);
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTabs = (SlidingTabLayout) getView().findViewById(R.id.tab);
        //mTabs.setCustomTabView(R.layout.tab_item, R.id.txv_tab_title);
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mPager.setCurrentItem(requestCode);
        Log.d(TAG, "onActivityResult()");
    }

    private void prepareDetailFragment(int position) {
        Fragment fragment = null;
        Class fragmentClass = null;
        String tag = "";

        switch (position) {
            case FRAGMENT_KEY_SUCCESS:
                fragmentClass = SuccessDetailFragment.class;
                tag = SuccessDetailFragment.TAG;
                break;
            case FRAGMENT_KEY_TWEETS:
                fragmentClass = TweetsDetailFragment.class;
                tag = TweetsDetailFragment.TAG;
                break;
            case FRAGMENT_KEY_SUCCESS_FAVORITE:
                fragmentClass = SuccessDetailFragment.class;
                tag = SuccessDetailFragment.TAG;
                break;
            case FRAGMENT_KEY_TWEETS_FAVORITE:
                fragmentClass = TweetsDetailFragment.class;
                tag = TweetsDetailFragment.TAG;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        fragment.setArguments(new Bundle());

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frl_fragment_container, fragment, tag).commit();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] tabTitles;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            tabTitles = mContext.getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case FRAGMENT_KEY_SUCCESS:
                    return SuccessListFragment.getInstance(index);
                case FRAGMENT_KEY_TWEETS:
                    return TweetsListFragment.getInstance(index);
                case FRAGMENT_KEY_SUCCESS_FAVORITE:
                    return SuccessListFavoriteFragment.getInstance(index);
                case FRAGMENT_KEY_TWEETS_FAVORITE:
                    return TweetsListFavoriteFragment.getInstance(index);
                default:
                    return SuccessListFragment.getInstance(index);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
