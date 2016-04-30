package com.takahidesato.android.promatchandroid.network;

import android.util.Base64;

import com.takahidesato.android.promatchandroid.MainActivity;

import java.io.UnsupportedEncodingException;

/**
 * Created by tsato on 4/22/16.
 */
public class Util {
    /***** Twitter *****/
    public static final String BASE_TWITTER_URL = "https://api.twitter.com";
    public static final String TWITTER_PATH = "/1.1/statuses/user_timeline.json";
    public static final String TWITTER_OATH2_PATH = "/oauth2/token";

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String FIELD_GRANT_TYPE = "grant_type";
    public static final String FIELD_SCREEN_NAME = "screen_name";
    public static final String FIELD_COUNT = "count";

    public static final String GRANT_TYPE = "client_credentials";
    public static final String SCREEN_NAME = "novapromatchsv";
    public static final String COUNT = "50";

    /*
    Reference:
    https://dev.twitter.com/oauth/application-only
     */
    public static String getEncodedBearerTokenCredential(String key, String secret) {
        String tmp = key.concat(":").concat(secret);
        return encodeToBase64(tmp);
    }

    public static String encodeToBase64(String string) {
        byte[] data = null;
        try {
            data = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    /***** YouTube *****/
    public static final String BASE_GOOGLE_URL = "https://www.googleapis.com";
    public static final String YOUTUBE_PATH = "/youtube/v3/playlistItems";

    public static final String FIELD_PART = "part";
    public static final String FIELD_MAX_RESULTS = "maxResults";
    public static final String FIELD_PLAYLIST_ID = "playlistId";
    public static final String FIELD_KEY = "key";

    public static final String PART = "snippet";
    public static final String MAX_RESULTS = "50";
    public static final String PLAYLIST_ID = "PLYcNDuULX6syIPJYaDj9ieAIUjU7uiI6P";
    public static final String KEY = MainActivity.YOUTUBE_API_KEY;
}