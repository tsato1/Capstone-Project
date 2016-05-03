package com.takahidesato.android.promatchandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takahidesato.android.promatchandroid.tab.SlidingTabLayout;

/**
 * Created by tsato on 4/15/16.
 */
public class ViewPagerFragment extends Fragment {
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

        mAdatper = new ViewPagerAdapter(getFragmentManager());

        mPager = (ViewPager) getView().findViewById(R.id.view_pager);
        mPager.setAdapter(mAdatper);

        mTabs = (SlidingTabLayout) getView().findViewById(R.id.tab);
        //mTabs.setCustomTabView(R.layout.tab_item, R.id.txv_tab_title);
        mTabs.setDistributeEvenly(true);
        mTabs.setViewPager(mPager);
    }

    public void setCurrentItem(int page) {
        mPager.setCurrentItem(page);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
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
                case 0:
                    return SuccessStoriesListFragment.getInstance(index);
                case 1:
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
