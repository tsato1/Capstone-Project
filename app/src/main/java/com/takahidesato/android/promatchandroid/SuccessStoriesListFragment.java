package com.takahidesato.android.promatchandroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takahidesato.android.promatchandroid.ui.SuccessItem;
import com.takahidesato.android.promatchandroid.ui.SuccessStoriesRecyclerAdapter;
import com.takahidesato.android.promatchandroid.ui.TweetItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tsato on 4/14/16.
 */
public class SuccessStoriesListFragment extends Fragment {
    private static final String TAG = SuccessStoriesListFragment.class.getSimpleName();
    private static final String KEY = "position";

    @Bind(R.id.rcv_success)
    RecyclerView mRecyclerView;

    private SuccessStoriesRecyclerAdapter mSuccessStoriesRecyclerAdapter = null;
    private List<SuccessItem> mSuccessList = new ArrayList<>();
    private List<TweetItem> mTweetItems = new ArrayList<>();

    public static SuccessStoriesListFragment getInstance(int position) {
        SuccessStoriesListFragment fragment = new SuccessStoriesListFragment();
        Bundle args = new Bundle();
        args.putInt(KEY, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_success, container, false);
        Bundle args = getArguments();
        if (args != null) Log.i(TAG, "Fragment position = " + args.getInt(KEY));
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /***** determining column count for staggered grid view *****/
        int columnCount = 1;
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            columnCount = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                columnCount = 3;
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                columnCount = 2;
            }
        }
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mSuccessStoriesRecyclerAdapter = new SuccessStoriesRecyclerAdapter(getContext(), mSuccessList, MainActivity.URL_SUCCESS);
        mRecyclerView.setAdapter(mSuccessStoriesRecyclerAdapter);

        SuccessItem item = new SuccessItem();
        item.title = "Test";
        mSuccessList.add(item);

        mSuccessStoriesRecyclerAdapter.notifyDataSetChanged();
    }


}
