package com.takahidesato.android.promatchandroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.takahidesato.android.promatchandroid.ui.TweetsItem;
import com.takahidesato.android.promatchandroid.ui.TweetsRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tsato on 4/15/16.
 */
public class TweetsListFragment extends Fragment {
    private static final String TAG = TweetsListFragment.class.getSimpleName();
    private static final String KEY = "position";

    @Bind(R.id.srl_tweets)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rcv_tweets)
    RecyclerView mRecyclerView;

    private TweetsRecyclerAdapter mTweetsRecyclerAdapter = null;
    private List<TweetsItem> mTweetList = new ArrayList<>();

    private static String sAuthorizationOAuth = "";
    private static String sAuthorizationCall = "";

    public static TweetsListFragment getInstance(int position) {
        TweetsListFragment fragment = new TweetsListFragment();
        Bundle args = new Bundle();
        args.putInt(KEY, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("oauth", sAuthorizationOAuth);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_tweets, container, false);
        Bundle args = getArguments();
        if (args != null) Log.i(TAG, "Fragment position = " + args.getInt(KEY));
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
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mTweetsRecyclerAdapter = new TweetsRecyclerAdapter(getContext(), mTweetList);
        mRecyclerView.setAdapter(mTweetsRecyclerAdapter);

        /***** onRestoreState() *****/
        if (savedInstanceState != null) {
            sAuthorizationOAuth = savedInstanceState.getString("oauth");
        }

        //authorize();
    }

    private void authorize() {
        TwitterApi twitterApi = TwitterServiceGenerator.createService(TwitterApi.class, sAuthorizationOAuth, Util.BASE_TWITTER_URL);

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
                }
            }

            @Override
            public void onFailure(Call<TwitterAccessToken> call, Throwable t) {
                Log.e(TAG, "Retrofit Twitter OAuth Error: " + t.toString());
            }
        });
    }

    private void retrieveData() {
        TwitterApi twitterApi = TwitterServiceGenerator.createService(TwitterApi.class, sAuthorizationCall, Util.BASE_TWITTER_URL);

        Call<List<TwitterResponseBody>> call = twitterApi.getTweets(Util.SCREEN_NAME, Util.COUNT);
        call.enqueue(new Callback<List<TwitterResponseBody>>() {
            @Override
            public void onResponse(Call<List<TwitterResponseBody>> call, Response<List<TwitterResponseBody>> response) {
                Log.d("Retrofit Twitter call", "response.code()="+response.code());
                if (response.code() == 200) {
                    List<TwitterResponseBody> body = response.body();
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

                    //mTweetsRecyclerAdapter.refresAdapter(mTweetList);
                }
            }

            @Override
            public void onFailure(Call<List<TwitterResponseBody>> call, Throwable t) {
                Log.e(TAG, "Retrofit Twitter call Error: " + t.toString());
            }
        });
    }
}
