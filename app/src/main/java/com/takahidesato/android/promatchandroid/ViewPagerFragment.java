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

import com.takahidesato.android.promatchandroid.tab.SlidingTabLayout;

/**
 * Created by tsato on 4/15/16.
 */
public class ViewPagerFragment extends Fragment {
    public static final String FRAGMENT_KEY = "fragment_key";
    public static final int FRAGMENT_KEY_SUCCESS = 0;
    public static final int FRAGMENT_KEY_TWEETS = 1;

    private static final String TAG = ViewPagerFragment.class.getSimpleName();
    private static final int NUM_ITEMS = 2;

    private Context mContext;

    private ViewPagerAdapter mAdatper;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

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
        mPager.setCurrentItem(requestCode);
    }

    private void prepareDetailFragment(int position) {
        Fragment fragment = null;
        Class fragmentClass = null;
        String tag = "";

        switch (position) {
            case FRAGMENT_KEY_SUCCESS:
                fragmentClass = SuccessStoriesDetailFragment.class;
                tag = SuccessStoriesDetailFragment.TAG;
                break;
            case FRAGMENT_KEY_TWEETS:
                fragmentClass = TweetsDetailFragment.class;
                tag = TweetsDetailFragment.TAG;
                break;
            default:
                fragmentClass = SuccessStoriesDetailFragment.class;
                tag = SuccessStoriesDetailFragment.TAG;
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
                    return SuccessStoriesListFragment.getInstance(index);
                case FRAGMENT_KEY_TWEETS:
                    return TweetsListFragment.getInstance(index);
                default:
                    return SuccessStoriesListFragment.getInstance(index);
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
