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

import com.takahidesato.android.promatchandroid.adapter.SuccessItem;
import com.takahidesato.android.promatchandroid.adapter.SuccessRecyclerAdapter;
import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBSuccessLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tsato on 5/11/16.
 */
public class SuccessListFavoriteFragment extends Fragment
        implements SuccessRecyclerAdapter.OnCardItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = SuccessListFavoriteFragment.class.getSimpleName();

    @Bind(R.id.srl_success)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rcv_success)
    RecyclerView mRecyclerView;

    private SuccessRecyclerAdapter mSuccessRecyclerAdapter = null;
    private List<SuccessItem> mSuccessFavoriteList = new ArrayList<>();
    private boolean mIsDualPane;
    private SuccessItem mSuccessItem;
    private Cursor mCursor;

    public static Fragment getInstance(int key) {
        Fragment fragment = new SuccessListFavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ViewPagerFragment.FRAGMENT_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_success, container, false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstatnceState) {
        super.onActivityCreated(savedInstatnceState);

        getLoaderManager().initLoader(0, null, this);

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

        mIsDualPane = MainActivity.IS_DUAL_PANE;

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mSuccessRecyclerAdapter = new SuccessRecyclerAdapter(getContext(), mSuccessFavoriteList);
        mSuccessRecyclerAdapter.setOnCardItemClickListener(this);
        mRecyclerView.setAdapter(mSuccessRecyclerAdapter);

        if (savedInstatnceState != null) {
            mSuccessItem = savedInstatnceState.getParcelable("item");
        }

        reloadData();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("item", mSuccessItem);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void reloadData() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onCardItemClick(int position) {
        Log.d(TAG, "onCardItemSelected(): Card Position = " + position);

        if (mIsDualPane) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SuccessDetailFragment fragment = (SuccessDetailFragment) manager.findFragmentByTag(SuccessDetailFragment.TAG);
            Bundle args = fragment.getArguments();
            args.putInt(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_SUCCESS_FAVORITE);
            args.putParcelable("item", mSuccessFavoriteList.get(position));
            fragment.setUpLayout();
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_SUCCESS_FAVORITE);
            intent.putExtra("item", mSuccessFavoriteList.get(position));
            getParentFragment().startActivityForResult(intent, ViewPagerFragment.FRAGMENT_KEY_SUCCESS_FAVORITE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return DBSuccessLoader.newInstanceForAll(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;
        mSuccessFavoriteList.clear();

        if (mCursor.moveToFirst()) {
            do {
                SuccessItem item = new SuccessItem(
                        mCursor.getInt(mCursor.getColumnIndex(DBColumns._ID)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_ID_ITEM)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_PUBLISHED_AT)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_CHANNEL_ID)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_TITLE)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_DESCRIPTION)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_THUMBNAIL_DEFAULT_URL)),
                        mCursor.getString(mCursor.getColumnIndex(DBColumns.COL_THUMBNAIL_MEDIUM_URL))
                );
                mSuccessFavoriteList.add(item);
                mSuccessItem = item;
                //Log.d(TAG, "_id = " + item.id + " : " + item.title);
            } while (mCursor.moveToNext());
        }
        mSuccessRecyclerAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        mSuccessRecyclerAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false); //todo null point ex here
    }
}
