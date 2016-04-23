package com.takahidesato.android.promatchandroid.network;

/**
 * Created by tsato on 4/22/16.
 */
public class Util {
    //RETROFIT
    public static final String BASE_TWITTER_URL = "https://api.twitter.com";
    public static final String TWITTER_PATH = "/1.1/statuses/home_timeline.json";
    public static final String SCREEN_NAME = "screen_name";
    public static final String COUNT = "count";

    public static final String TWITTER_OATH2_PATH = "/oauth2/token";
    public static final String GRANT_TYPE = "client_credentials";
}