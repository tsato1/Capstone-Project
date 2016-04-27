package com.takahidesato.android.promatchandroid.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tsato on 4/26/16.
 */
public class YouTubeResponseBody {
    public String kind;
    public String etag;
    public String nextPageToken;
    public List<Item> items;

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
                public Medium medium;

                public class Default {
                    public String url;
                    public String width;
                    public String height;
                }
                public class Medium {
                    public String url;
                    public String width;
                    public String height;
                }
            }
        }
    }
}
