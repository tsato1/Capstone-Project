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
            "Authorization: ",
            "Content-Type: application/x-www-form-urlencoded;charset=UTF-8"
    })
    Call<TwitterAccessToken> getAccessToken(@Field(Util.FIELD_GRANT_TYPE) String grantType);

    @GET(Util.TWITTER_PATH)
    Call<List<TweetItem>> getTweets(@Query(Util.FIELD_SCREEN_NAME) String screen_name,
                                    @Query(Util.FIELD_COUNT) String count);
}
