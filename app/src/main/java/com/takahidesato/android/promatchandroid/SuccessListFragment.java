package com.takahidesato.android.promatchandroid;

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
import android.widget.TextView;

import com.takahidesato.android.promatchandroid.network.Util;
import com.takahidesato.android.promatchandroid.network.YouTubeApi;
import com.takahidesato.android.promatchandroid.network.YouTubeResponseBody;
import com.takahidesato.android.promatchandroid.network.YouTubeServiceGenerator;
import com.takahidesato.android.promatchandroid.adapter.SuccessItem;
import com.takahidesato.android.promatchandroid.adapter.SuccessRecyclerAdapter;

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
public class SuccessListFragment extends Fragment implements SuccessRecyclerAdapter.OnCardItemClickListener {
    public static final String TAG = SuccessListFragment.class.getSimpleName();

    @Bind(R.id.srl_success)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rcv_success)
    RecyclerView mRecyclerView;
    @Bind(R.id.txv_network_alert)
    TextView mNetworkAlertTextView;

    private SuccessRecyclerAdapter mSuccessRecyclerAdapter = null;
    private List<SuccessItem> mSuccessList = new ArrayList<>();
    private boolean mIsDualPane;
    private SuccessItem mSuccessItem;

    public static SuccessListFragment getInstance(int key) {
        SuccessListFragment fragment = new SuccessListFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.FRAGMENT_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_success, container, false);
        Bundle args = getArguments();
        if (args != null) Log.i(TAG, "Fragment position = " + args.getInt(MainActivity.FRAGMENT_KEY));
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

        mSuccessRecyclerAdapter = new SuccessRecyclerAdapter(getContext(), mSuccessList);
        mSuccessRecyclerAdapter.setOnCardItemClickListener(this);
        mRecyclerView.setAdapter(mSuccessRecyclerAdapter);

        if (savedInstanceState != null) {
            mSuccessItem = savedInstanceState.getParcelable("item");
        }

        retrieveData();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("item", mSuccessItem);
        super.onSaveInstanceState(savedInstanceState);
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
                    //logDebug(body);
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
                                body.items.get(i).snippet.thumbnails.medium.url,
                                body.items.get(i).snippet.thumbnails.high.url,
                                body.items.get(i).snippet.resourceId.videoId,
                                ""
                        );
                        mSuccessList.add(item);
                        mSuccessItem = item;
                    }
                    mSuccessRecyclerAdapter.notifyDataSetChanged();
                    if (mRecyclerView != null) mRecyclerView.setVisibility(View.VISIBLE);
                    if (mNetworkAlertTextView != null) mNetworkAlertTextView.setVisibility(View.GONE);
                } else {
                    if (mRecyclerView != null) mRecyclerView.setVisibility(View.GONE);
                    if (mNetworkAlertTextView != null) mNetworkAlertTextView.setVisibility(View.VISIBLE);
                }
                if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<YouTubeResponseBody> call, Throwable t) {
                Log.e(TAG, "Retrofit YouTube Error: " + t.toString());
                if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
                if (mRecyclerView != null) mRecyclerView.setVisibility(View.GONE);
                if (mNetworkAlertTextView != null) mNetworkAlertTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onCardItemClick(int position) {
        Log.d(TAG, "onCardItemSelected(): Card Position = " + position + mIsDualPane);

        if (mIsDualPane) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SuccessDetailFragment fragment = (SuccessDetailFragment) manager.findFragmentByTag(SuccessDetailFragment.TAG);
            Bundle args = fragment.getArguments();
            args.putInt(MainActivity.FRAGMENT_KEY, MainActivity.FRAGMENT_KEY_SUCCESS);
            args.putParcelable("item", mSuccessList.get(position));
            fragment.setUpLayout();
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(MainActivity.FRAGMENT_KEY, MainActivity.FRAGMENT_KEY_SUCCESS);
            intent.putExtra("item", mSuccessList.get(position));
            getParentFragment().startActivityForResult(intent, MainActivity.FRAGMENT_KEY_SUCCESS);
        }
    }

    private void logDebug(YouTubeResponseBody body) {
        for (int i = 0; i < body.items.size(); i++) {
            Log.d("Retrofit YouTube",
                    "id="+body.items.get(i).id + ", " +
                    "title="+body.items.get(i).snippet.title +", " +
                    "description="+body.items.get(i).snippet.description +", " +
                    "thumbnail default url="+body.items.get(i).snippet.thumbnails.defaultSize.url+", " +
                    "thumbnail medium url="+body.items.get(i).snippet.thumbnails.medium.url +", " +
                    "thumbnail high url="+body.items.get(i).snippet.thumbnails.high.url + ", " +
                    "videoId="+body.items.get(i).snippet.resourceId.videoId
            );
        }
    }
}
