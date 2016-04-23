package com.takahidesato.android.promatchandroid.network;

import com.takahidesato.android.promatchandroid.TweetsListFragment;
import com.takahidesato.android.promatchandroid.ui.TweetItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by tsato on 4/21/16.
 */
public interface TwitterApi {
    @FormUrlEncoded
    @POST(Util.TWITTER_OATH2_PATH)
    @Headers({
            "",
            "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"
    })
    Call<TwitterAccessToken> getAccessToken(@Field("grant_type") String grantType);

    @GET(Util.TWITTER_PATH)
    Call<List<TweetItem>> getTweets(@Query(Util.SCREEN_NAME) String screen_name,
                                    @Query(Util.COUNT) String count);


//    @GET("/1.1/statuses/user_timeline.json?")
//    Call<List<TweetItem>> getTweetList(
//            @Query("screen_name") String screenName,
//            @Query("count") int count);
//
    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    Call<TweetsListFragment.StackOverflowQuestions> getStack(@Query("tagged") String tags);
}
