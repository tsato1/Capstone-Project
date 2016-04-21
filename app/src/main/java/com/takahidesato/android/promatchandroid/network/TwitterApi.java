package com.takahidesato.android.promatchandroid.network;

import com.takahidesato.android.promatchandroid.MainActivity;
import com.takahidesato.android.promatchandroid.ui.TweetItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by tsato on 4/21/16.
 */
public interface TwitterApi {
    @GET(MainActivity.URL_TWEETS_TOKEN)
    Call<List<TweetItem>> listTweets(
            @Query("id") int id,
            @Query("screen_name") String screenName);
}
