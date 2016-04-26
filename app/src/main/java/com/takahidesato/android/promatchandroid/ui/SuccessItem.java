package com.takahidesato.android.promatchandroid.ui;

import android.provider.MediaStore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tsato on 4/17/16.
 */
public class SuccessItem {
    public String kind;
    public String etag;
    public String nextPageToken;
    public List<Item> items;

    public SuccessItem (List<Item> items) {
        this.items = items;
    }

    public class Item {
        public String kind;
        public String etag;
        public String id;
        public Snippet snippet;

        public class Snippet {
            public String publishedAt;
            public String channelId;
            public String title;
            public String description;
            public Thumbnails thumbnails;

            public class Thumbnails {
                @SerializedName(value="default")
                public Default defaultSize;

                public class Default {
                    public String url;
                    public String width;
                    public String height;
                }
            }
        }
    }
}
