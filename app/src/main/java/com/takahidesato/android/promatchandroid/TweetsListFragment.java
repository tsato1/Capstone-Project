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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takahidesato.android.promatchandroid.network.JSONDeserializer;
import com.takahidesato.android.promatchandroid.network.TwitterAccessToken;
import com.takahidesato.android.promatchandroid.network.TwitterApi;
import com.takahidesato.android.promatchandroid.network.Util;
import com.takahidesato.android.promatchandroid.ui.TweetItem;
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
    private List<TweetItem> mTweetList = new ArrayList<>();

    private static String sAuthorization;

    public static TweetsListFragment getInstance(int position) {
        TweetsListFragment fragment = new TweetsListFragment();
        Bundle args = new Bundle();
        args.putInt(KEY, position);
        fragment.setArguments(args);
        return fragment;
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

        retrieveData();
    }

    private void retrieveData() {
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(TweetItem.User.class, new JSONDeserializer<TweetItem.User>())
//                .registerTypeAdapter(TweetItem.Entities.class, new JSONDeserializer<TweetItem.Entities>())
//                .registerTypeAdapter(TweetItem.Entities.Media.class, new JSONDeserializer<TweetItem.Entities.Media>())
//                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.BASE_TWITTER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TwitterApi twitterApi = retrofit.create(TwitterApi.class);
        Call<TwitterAccessToken> call = twitterApi.getAccessToken(Util.GRANT_TYPE);
        call.enqueue(new Callback<TwitterAccessToken>() {
            @Override
            public void onResponse(Call<TwitterAccessToken> call, Response<TwitterAccessToken> response) {
                int code = response.code();
                Log.d("test", "access code = "+code);
                TwitterAccessToken body = response.body();
                sAuthorization = body.getTokentype().concat(" ").concat(body.getAccessToken());
                Log.d("test", "access token = "+body.getAccessToken());
                Log.d("test", "access type = "+body.getTokentype());

                anotherMethod();
            }

            @Override
            public void onFailure(Call<TwitterAccessToken> call, Throwable t) {
                Log.e(TAG, "Error: " + t.toString());
            }
        });
    }

    private void anotherMethod() {
        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader(Util.HEADER_AUTHORIZATION, sAuthorization)
                                .method(chain.request().method(), chain.request().body())
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Util.BASE_TWITTER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(defaultHttpClient)
                .build();

        TwitterApi twitterApi = retrofit.create(TwitterApi.class);
        Call<List<TweetItem>> call = twitterApi.getTweets(Util.SCREEN_NAME, Util.COUNT);
        call.enqueue(new Callback<List<TweetItem>>() {
            @Override
            public void onResponse(Call<List<TweetItem>> call, Response<List<TweetItem>> response) {
                if (response.code() == 200) {
                    Log.d("test", "response = "+response.code());

                    mTweetList = response.body();

                    for (int i = 0; i < mTweetList.size(); ++i) {
                        String media_url = mTweetList.get(i).entities.media != null && mTweetList.get(i).entities.media.length != 0
                                ? mTweetList.get(i).entities.media[0].media_url : "";
                        String hashtag = mTweetList.get(i).entities.hashtags != null && mTweetList.get(i).entities.hashtags.length != 0
                                ? mTweetList.get(i).entities.hashtags[0].text : "";
                        Log.d("test", "create_at=" + mTweetList.get(i).created_at +
                                ", id=" + mTweetList.get(i).id_str +
                                ", name=" + mTweetList.get(i).user.name +
                                ", screen_name=" + mTweetList.get(i).user.screen_name +
                                ", profile_image_url=" + mTweetList.get(i).user.profile_image_url +
                                ", media_url=" + media_url +
                                ", hashtags=" + hashtag +
                                ", text=" + mTweetList.get(i).text);
                    }

                    mTweetsRecyclerAdapter.refresAdapter(mTweetList);
                }
            }

            @Override
            public void onFailure(Call<List<TweetItem>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.toString());
            }
        });
    }
}
