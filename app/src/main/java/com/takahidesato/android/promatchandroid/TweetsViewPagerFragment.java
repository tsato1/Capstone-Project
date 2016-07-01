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
public class TweetsViewPagerFragment extends Fragment {
    private enum fragment {
        TWEETS,
        TWEETS_FAVORITE
    }

    private static final String TAG = TweetsViewPagerFragment.class.getSimpleName();
    private static final int NUM_ITEMS = 2;

    private Context mContext;

    private ViewPagerAdapter mAdatper;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private Tracker mTracker;
    private Fragment mCurrentFragment;

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

    private void prepareDetailFragment(int position) {
        Fragment fragment = null;
        Class fragmentClass = TweetsDetailFragment.class;
        String tag = TweetsDetailFragment.TAG;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        fragment.setArguments(new Bundle());

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frl_fragment_detail_container, fragment, tag).commit();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] tabTitles;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            tabTitles = mContext.getResources().getStringArray(R.array.tabs_tweets);
        }

        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case 0:
                    return TweetsListFragment.getInstance(index);
                case 1:
                    return TweetsListFavoriteFragment.getInstance(index);
                default:
                    return TweetsListFragment.getInstance(index);
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

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (mCurrentFragment != object) {
                mCurrentFragment = (Fragment) object;

                /* if current fragment is favorite fragment, reload data from database */
                if (mCurrentFragment.getArguments().getInt(MainActivity.FRAGMENT_KEY)%10 == 1) {
                    ((TweetsListFavoriteFragment) mCurrentFragment).reloadData();
                }
            }
            super.setPrimaryItem(container, position, object);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mPager.setCurrentItem(requestCode%10);
        Log.d(TAG, "onActivityResult()");
    }
}
