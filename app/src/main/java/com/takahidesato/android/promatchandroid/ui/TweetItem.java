package com.takahidesato.android.promatchandroid.ui;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tsato on 4/17/16.
 */
public class TweetItem {
    /* tweet */
    public String created_at;
    public String id_str;
    public String text;
    public User user;
    public Entities entities;

    /* user */
    public class User {
        public String id_str;
        public String name;
        public String screen_name;
        public String profile_image_url;
    }

    public class Entities {
        public Hashtag[] hashtags;
        public Media[] media;
        /* media */
        public class Media {
            public String id_str;
            public String media_url;
        }
        public class Hashtag {
            public String text;
        }
    }
}
