package com.takahidesato.android.promatchandroid.ui;

/**
 * Created by tsato on 4/17/16.
 */
public class TweetItem {
    /* tweet */
    public String created_at;
    public String id_str;
    public String text;

    /* user */
    public class user {
        public String id_str;
        public String name;
        public String screen_name;
        public String profile_image_url;
    }

    /* media */
    public class media {
        public String media_url;
    }
}
