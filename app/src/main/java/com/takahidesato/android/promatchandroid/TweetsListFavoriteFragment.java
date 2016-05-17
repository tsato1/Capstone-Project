package com.takahidesato.android.promatchandroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takahidesato.android.promatchandroid.adapter.TweetsItem;
import com.takahidesato.android.promatchandroid.adapter.TweetsRecyclerAdapter;
import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBTweetsLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tsato on 5/11/16.
 */
public class TweetsListFavoriteFragment extends Fragment
        implements TweetsRecyclerAdapter.OnCardItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = TweetsListFavoriteFragment.class.getSimpleName();

    @Bind(R.id.srl_tweets)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rcv_tweets)
    RecyclerView mRecyclerView;

    private TweetsRecyclerAdapter mTweetsRecyclerAdapter = null;
    private List<TweetsItem> mTweetsFavoriteList = new ArrayList<>();
    private boolean mIsDualPane;
    private Cursor mCursor;

    public static Fragment getInstance(int key) {
        Fragment fragment = new TweetsListFavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ViewPagerFragment.FRAGMENT_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_favorite_tweets, container, false);
        ButterKnife.bind(this, view);
        Bundle args = getArguments();
        if (args != null) Log.i(TAG, "Fragment position = " + args.getInt(ViewPagerFragment.FRAGMENT_KEY));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });

        return view;
    }

    public void reloadData() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /***** determining column count for staggered grid view *****/
        int columnCount = 1;
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            columnCount = 2;
            /***if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {columnCount = 3;}***/
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                columnCount = 2;
            }
        }

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mTweetsRecyclerAdapter = new TweetsRecyclerAdapter(getContext(), mTweetsFavoriteList);
        mTweetsRecyclerAdapter.setOnCardItemClickListener(this);
        mRecyclerView.setAdapter(mTweetsRecyclerAdapter);
    }


    @Override
    public void onCardItemClick(int position) {
        Log.d(TAG, "onCardItemSelected(): Card Position = " + position);

        if (mIsDualPane) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SuccessDetailFragment fragment = (SuccessDetailFragment) manager.findFragmentByTag(SuccessDetailFragment.TAG);
            Bundle args = fragment.getArguments();
            args.putInt(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_TWEETS_FAVORITE);
            args.putParcelable("item", mTweetsFavoriteList.get(position));
            fragment.setUpLayout();
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_TWEETS_FAVORITE);
            intent.putExtra("item", mTweetsFavoriteList.get(position));
            getParentFragment().startActivityForResult(intent, ViewPagerFragment.FRAGMENT_KEY_TWEETS_FAVORITE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return DBTweetsLoader.newInstanceForAll(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;
        mTweetsFavoriteList.clear();

        if (mCursor.moveToFirst()) {
            do {
                TweetsItem item = new TweetsItem(
                        mCursor.getInt(mCursor.getColumnIndex(DBColumns._ID)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_ID_STR)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_CREATED_AT)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_TEXT)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_SCREEN_NAME)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_PROFILE_IMAGE_URL)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_MEDIA_IMAGE_URL))
                );
                mTweetsFavoriteList.add(item);
            } while (mCursor.moveToNext());
        }
        mTweetsRecyclerAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        mTweetsRecyclerAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
