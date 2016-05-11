package com.takahidesato.android.promatchandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takahidesato.android.promatchandroid.network.Util;
import com.takahidesato.android.promatchandroid.network.YouTubeApi;
import com.takahidesato.android.promatchandroid.network.YouTubeResponseBody;
import com.takahidesato.android.promatchandroid.network.YouTubeServiceGenerator;
import com.takahidesato.android.promatchandroid.ui.SuccessItem;
import com.takahidesato.android.promatchandroid.ui.SuccessStoriesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tsato on 4/14/16.
 */
public class SuccessStoriesListFragment extends Fragment implements SuccessStoriesRecyclerAdapter.OnCardItemClickListener {
    private static final String TAG = SuccessStoriesListFragment.class.getSimpleName();

    @Bind(R.id.srl_success)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rcv_success)
    RecyclerView mRecyclerView;

    private SuccessStoriesRecyclerAdapter mSuccessStoriesRecyclerAdapter = null;
    private List<SuccessItem> mSuccessList = new ArrayList<>();
    private boolean mIsDualPane;

    public static SuccessStoriesListFragment getInstance(int key) {
        SuccessStoriesListFragment fragment = new SuccessStoriesListFragment();
        Bundle args = new Bundle();
        args.putInt(ViewPagerFragment.FRAGMENT_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_success, container, false);
        Bundle args = getArguments();
        if (args != null) Log.i(TAG, "Fragment position = " + args.getInt(ViewPagerFragment.FRAGMENT_KEY));
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /***** determining column count for staggered grid view *****/
        int columnCount = 1;
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        Log.d("test", "screenSize="+screenSize);

        //mIsDualPane = (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE)? true: false;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            columnCount = 2;
            /***if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                columnCount = 3;
            }***/
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                columnCount = 2;
            }
        }

        View detailFragment = getActivity().findViewById(R.id.frl_fragment_container);
        mIsDualPane = detailFragment != null && detailFragment.getVisibility() == View.VISIBLE;

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mSuccessStoriesRecyclerAdapter = new SuccessStoriesRecyclerAdapter(getContext(), mSuccessList);
        mSuccessStoriesRecyclerAdapter.setOnCardItemClickListener(this);
        mRecyclerView.setAdapter(mSuccessStoriesRecyclerAdapter);

//        if (savedInstanceState != null) {
//            mMovieItem = savedInstanceState.getParcelable("item");
//            url = savedInstanceState.getString("url");
//            pageCode = savedInstanceState.getInt("isFavoriteShown");
//        }
//
//        if (pageCode == CODE_FAVORITE) loadFromDatabase();
//        else fetchFromCloud();

        //TODO wifi / network check
        retrieveData();
    }

    private void retrieveData() {
        YouTubeApi youTubeApi = YouTubeServiceGenerator.createService(YouTubeApi.class);

        Call<YouTubeResponseBody> call = youTubeApi.getSuccess(
                Util.PART,
                Util.MAX_RESULTS,
                Util.PLAYLIST_ID,
                Util.KEY);

        call.enqueue(new Callback<YouTubeResponseBody>() {
            @Override
            public void onResponse(Call<YouTubeResponseBody> call, Response<YouTubeResponseBody> response) {
                Log.d("Retrofit YouTube", "response.code()="+response.code());

                if (response.code() == 200) {
                    YouTubeResponseBody body = response.body();
                    logDebug(body);
                    mSuccessList.clear();
                    for (int i = 0; i < body.items.size(); i++) {
                        SuccessItem item = new SuccessItem(
                                -1,
                                body.items.get(i).id,
                                body.items.get(i).snippet.publishedAt,
                                body.items.get(i).snippet.channelId,
                                body.items.get(i).snippet.title,
                                body.items.get(i).snippet.description,
                                body.items.get(i).snippet.thumbnails.defaultSize.url,
                                body.items.get(i).snippet.thumbnails.medium.url
                        );
                        mSuccessList.add(item);
                    }
                    mSuccessStoriesRecyclerAdapter.notifyDataSetChanged();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<YouTubeResponseBody> call, Throwable t) {
                Log.e(TAG, "Retrofit YouTube Error: " + t.toString());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCardItemSelected(int position) {
        Log.d(TAG, "onCardItemSelected(): Card Position = " + position);

        if (mIsDualPane) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SuccessStoriesDetailFragment fragment = (SuccessStoriesDetailFragment) manager.findFragmentByTag("TAG");
            Bundle args = fragment.getArguments();
            args.putInt(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_SUCCESS);
            args.putParcelable("item", mSuccessList.get(position));
            fragment.setUpLayout();
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_SUCCESS);
            intent.putExtra("item", mSuccessList.get(position));
            getParentFragment().startActivityForResult(intent, ViewPagerFragment.FRAGMENT_KEY_SUCCESS);
        }
    }

    private void logDebug(YouTubeResponseBody body) {
        for (int i = 0; i < body.items.size(); i++) {
            Log.d("Retrofit YouTube",
                    "id="+body.items.get(i).id + ", " +
                            "title="+body.items.get(i).snippet.title +", " +
                            "description="+body.items.get(i).snippet.description +", " +
                            "thumbnail default url="+body.items.get(i).snippet.thumbnails.defaultSize.url+", " +
                            "thumbnail medium url="+body.items.get(i).snippet.thumbnails.medium.url);
        }
    }
}
