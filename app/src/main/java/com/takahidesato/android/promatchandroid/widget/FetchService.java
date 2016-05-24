package com.takahidesato.android.promatchandroid.widget;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.takahidesato.android.promatchandroid.R;
import com.takahidesato.android.promatchandroid.adapter.TweetsItem;
import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBContentProvider;
import com.takahidesato.android.promatchandroid.network.TwitterAccessToken;
import com.takahidesato.android.promatchandroid.network.TwitterApi;
import com.takahidesato.android.promatchandroid.network.TwitterResponseBody;
import com.takahidesato.android.promatchandroid.network.TwitterServiceGenerator;
import com.takahidesato.android.promatchandroid.network.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tsato on 5/23/16.
 */
public class FetchService extends IntentService {
    public static final String TAG = FetchService.class.getSimpleName();
    public FetchService()
    {
        super(FetchService.class.getSimpleName());
    }

    private static String sAuthorizationOAuth = "";
    private static String sAuthorizationCall = "";

    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.d(TAG,"onHandleIntent() called");
        authorize();
        return;
    }

    private void authorize() {
        TwitterApi twitterApi = TwitterServiceGenerator.createService(TwitterApi.class, sAuthorizationOAuth);

        Call<TwitterAccessToken> call = twitterApi.getAccessToken(Util.GRANT_TYPE);
        call.enqueue(new Callback<TwitterAccessToken>() {
            @Override
            public void onResponse(Call<TwitterAccessToken> call, Response<TwitterAccessToken> response) {
                Log.d(TAG, "Retrofit Twitter OAuth: response.code()=" + response.code());
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
        TwitterApi twitterApi = TwitterServiceGenerator.createService(TwitterApi.class, sAuthorizationCall);

        Call<List<TwitterResponseBody>> call = twitterApi.getTweets(Util.SCREEN_NAME, Util.COUNT);
        call.enqueue(new Callback<List<TwitterResponseBody>>() {
            @Override
            public void onResponse(Call<List<TwitterResponseBody>> call, Response<List<TwitterResponseBody>> response) {
                Log.d(TAG, "Retrofit Twitter call: response.code()="+response.code());

                if (response.code() == 200) {
                    List<TwitterResponseBody> body = response.body();

                    Vector<ContentValues> values = new Vector<>(body.size());
                    ContentValues match_values = new ContentValues();
                    for (int i = 0; i < body.size(); ++i) {
                        match_values.put(DBColumns.COL_SCREEN_NAME, body.get(i).user.screen_name);
                        match_values.put(DBColumns.COL_CREATED_AT, body.get(i).created_at);
                        match_values.put(DBColumns.COL_TEXT, body.get(i).text);
                        values.add(match_values);
                    }

                    ContentValues[] insert_data = new ContentValues[values.size()];
                    values.toArray(insert_data);
                    int result = getApplicationContext().getContentResolver().bulkInsert(DBContentProvider.Contract.TABLE_TWEETS_REP.contentUri, insert_data);

                    Log.d(TAG,"Successfully Inserted : " + String.valueOf(result));
                }
            }

            @Override
            public void onFailure(Call<List<TwitterResponseBody>> call, Throwable t) {
                Log.e(TAG, "Retrofit Twitter call Error: " + t.toString());
            }
        });
    }
}
