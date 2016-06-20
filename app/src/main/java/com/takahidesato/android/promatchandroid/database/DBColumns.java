package com.takahidesato.android.promatchandroid.database;

import android.provider.BaseColumns;

/**
 * Created by tsato on 5/7/16.
 */
public class DBColumns implements BaseColumns {
    public static final String TABLE_SUCCESS_FAV = "table_success_fav"; // favorite
    public static final String COL_ID_ITEM = "_id_item";
    public static final String COL_PUBLISHED_AT = "_published_at";
    public static final String COL_CHANNEL_ID = "_channel_id";
    public static final String COL_TITLE = "_title";
    public static final String COL_DESCRIPTION = "_description";
    public static final String COL_THUMBNAIL_DEFAULT_URL = "_thumbnail_default_url";
    public static final String COL_THUMBNAIL_MEDIUM_URL = "_thumbnail_medium_url";
    public static final String COL_THUMBNAIL_HIGH_URL = "_thumbnail_high_url";
    public static final String COL_VIDEO_ID = "_video_id";
    public static final String COL_MEMO = "_memo";

    public static final String TABLE_TWEETS_FAV = "table_tweets_fav"; // favorite
    public static final String TABLE_TWEETS_REP = "table_tweets_rep"; // repository for widget
    public static final String COL_ID_STR = "_id_str";
    public static final String COL_CREATED_AT = "_created_at";
    public static final String COL_TEXT = "_text";
    public static final String COL_NAME = "_name";
    public static final String COL_SCREEN_NAME = "_screen_name";
    public static final String COL_PROFILE_IMAGE_URL = "_profile_image_url";
    public static final String COL_MEDIA_IMAGE_URL = "_media_image_url";
}
