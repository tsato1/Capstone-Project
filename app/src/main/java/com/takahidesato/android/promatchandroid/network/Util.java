package com.takahidesato.android.promatchandroid.network;

/**
 * Created by tsato on 4/22/16.
 */
public class Util {
    //RETROFIT
    public static final String BASE_TWITTER_URL = "https://api.twitter.com";
    public static final String TWITTER_PATH = "/1.1/statuses/user_timeline.json";
    public static final String TWITTER_OATH2_PATH = "/oauth2/token";

    public static final String HEADER_AUTHORIZATION = "Authorization";

    public static final String FIELD_GRANT_TYPE = "grant_type";
    public static final String FIELD_SCREEN_NAME = "screen_name";
    public static final String FIELD_COUNT = "count";

    public static final String GRANT_TYPE = "client_credentials";
    public static final String SCREEN_NAME = "novapromatchsv";
    public static final String COUNT = "10";
}