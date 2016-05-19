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

import com.takahidesato.android.promatchandroid.network.TwitterAccessToken;
import com.takahidesato.android.promatchandroid.network.TwitterApi;
import com.takahidesato.android.promatchandroid.network.TwitterResponseBody;
import com.takahidesato.android.promatchandroid.network.TwitterServiceGenerator;
import com.takahidesato.android.promatchandroid.network.Util;
import com.takahidesato.android.promatchandroid.adapter.TweetsItem;
import com.takahidesato.android.promatchandroid.adapter.TweetsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tsato on 4/15/16.
 */
public class TweetsListFragment extends Fragment implements TweetsRecyclerAdapter.OnCardItemClickListener {
    public static final String TAG = TweetsListFragment.class.getSimpleName();

    private static String sAuthorizationOAuth = "";
    private static String sAuthorizationCall = "";

    @Bind(R.id.srl_tweets)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rcv_tweets)
    RecyclerView mRecyclerView;

    private TweetsRecyclerAdapter mTweetsRecyclerAdapter = null;
    private List<TweetsItem> mTweetsList = new ArrayList<>();
    private boolean mIsDualPane;
    private TweetsItem mTweetsItem;

    public static TweetsListFragment getInstance(int key) {
        TweetsListFragment fragment = new TweetsListFragment();
        Bundle args = new Bundle();
        args.putInt(ViewPagerFragment.FRAGMENT_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_tweets, container, false);
        Bundle args = getArguments();
        if (args != null) Log.i(TAG, "Fragment position = " + args.getInt(ViewPagerFragment.FRAGMENT_KEY));
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                authorize();
            }
        });
        sAuthorizationOAuth = "Basic".concat(" ").concat(Util.getEncodedBearerTokenCredential(MainActivity.TWITTER_API_KEY, MainActivity.TWITTER_API_SECRET));

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

        mIsDualPane = MainActivity.IS_DUAL_PANE;

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mTweetsRecyclerAdapter = new TweetsRecyclerAdapter(getContext(), mTweetsList);
        mTweetsRecyclerAdapter.setOnCardItemClickListener(this);
        mRecyclerView.setAdapter(mTweetsRecyclerAdapter);

        if (savedInstanceState != null) {
            sAuthorizationOAuth = savedInstanceState.getString("oauth");
            mTweetsItem = savedInstanceState.getParcelable("item");
        }

        authorize();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("oauth", sAuthorizationOAuth);
        savedInstanceState.putParcelable("item", mTweetsItem);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void authorize() {
        TwitterApi twitterApi = TwitterServiceGenerator.createService(TwitterApi.class, sAuthorizationOAuth);

        Call<TwitterAccessToken> call = twitterApi.getAccessToken(Util.GRANT_TYPE);
        call.enqueue(new Callback<TwitterAccessToken>() {
            @Override
            public void onResponse(Call<TwitterAccessToken> call, Response<TwitterAccessToken> response) {
                Log.d("Retrofit Twitter OAuth", "response.code()=" + response.code());
                if (response.code() == 200) {
                    TwitterAccessToken body = response.body();
                    sAuthorizationCall = body.getTokentype().concat(" ").concat(body.getAccessToken());
                    Log.d("Retrofit Twitter OAuth", "access type  = " + body.getTokentype());
                    Log.d("Retrofit Twitter OAuth", "access token = " + body.getAccessToken());
                    retrieveData();
                } else {
                    if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<TwitterAccessToken> call, Throwable t) {
                Log.e(TAG, "Retrofit Twitter OAuth Error: " + t.toString());
                if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void retrieveData() {
        TwitterApi twitterApi = TwitterServiceGenerator.createService(TwitterApi.class, sAuthorizationCall);

        Call<List<TwitterResponseBody>> call = twitterApi.getTweets(Util.SCREEN_NAME, Util.COUNT);
        call.enqueue(new Callback<List<TwitterResponseBody>>() {
            @Override
            public void onResponse(Call<List<TwitterResponseBody>> call, Response<List<TwitterResponseBody>> response) {
                Log.d("Retrofit Twitter call", "response.code()="+response.code());

                if (response.code() == 200) {
                    List<TwitterResponseBody> body = response.body();
                    //logDebug(body);
                    mTweetsList.clear();
                    for (int i = 0; i < body.size(); ++i) {
                        TweetsItem item = new TweetsItem(
                                -1,
                                body.get(i).id_str,
                                body.get(i).created_at,
                                body.get(i).text,
                                body.get(i).user.name,
                                body.get(i).user.screen_name,
                                body.get(i).user.profile_image_url,
                                ""
                        );
                        mTweetsList.add(item);
                        mTweetsItem = item;
                    }
                    mTweetsRecyclerAdapter.notifyDataSetChanged();
                }
                if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<TwitterResponseBody>> call, Throwable t) {
                Log.e(TAG, "Retrofit Twitter call Error: " + t.toString());
                if (mSwipeRefreshLayout != null) mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCardItemClick(int position) {
        Log.d(TAG, "onCardItemSelected(): Card Position = " + position);

        if (MainActivity.IS_DUAL_PANE) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            TweetsDetailFragment fragment = (TweetsDetailFragment) manager.findFragmentByTag(TweetsDetailFragment.TAG);
            Bundle args = fragment.getArguments();
            args.putInt(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_SUCCESS);
            args.putParcelable("item", mTweetsList.get(position));
            fragment.setUpLayout();
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_TWEETS);
            intent.putExtra("item", mTweetsList.get(position));
            getParentFragment().startActivityForResult(intent, ViewPagerFragment.FRAGMENT_KEY_TWEETS);
        }
    }

    private void logDebug(List<TwitterResponseBody> body) {
        for (int i = 0; i < 10; ++i) {
            String media_url = body.get(i).entities.media != null && body.get(i).entities.media.size() != 0
                    ? body.get(i).entities.media.get(0).media_url : "";
            String hashtag = body.get(i).entities.hashtags != null && body.get(i).entities.hashtags.size() != 0
                    ? body.get(i).entities.hashtags.get(0).text : "";
            Log.d("Retrofit Twitter call", "create_at=" + body.get(i).created_at +
                    ", id=" + body.get(i).id_str +
                    ", name=" + body.get(i).user.name +
                    ", screen_name=" + body.get(i).user.screen_name +
                    ", profile_image_url=" + body.get(i).user.profile_image_url +
                    ", media_url=" + media_url +
                    ", hashtags=" + hashtag +
                    ", text=" + body.get(i).text);
        }
    }
}
