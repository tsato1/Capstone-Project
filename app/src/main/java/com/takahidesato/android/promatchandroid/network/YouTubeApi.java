package com.takahidesato.android.promatchandroid.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tsato on 4/24/16.
 */
public interface YouTubeApi {
    @GET(Util.YOUTUBE_PATH)
    Call<YouTubeResponseBody> getSuccess(@Query(Util.FIELD_PART) String part,
                                         @Query(Util.FIELD_MAX_RESULTS) String maxResults,
                                         @Query(Util.FIELD_PLAYLIST_ID) String playlistId,
                                         @Query(Util.FIELD_KEY) String key);
}
