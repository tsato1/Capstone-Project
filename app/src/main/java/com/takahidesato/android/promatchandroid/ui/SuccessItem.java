package com.takahidesato.android.promatchandroid.ui;

import java.util.List;

/**
 * Created by tsato on 4/26/16.
 */
public class SuccessItem {
    public String id; // for this app
    public String idItem; // made by YouTube
    public String publishedAt;
    public String channelId;
    public String title;
    public String description;
    public String thumbnailDefaultUrl;
    public String thumbnailMediumUrl;

    public SuccessItem(String id,
                       String idItem,
                       String publishedAt,
                       String channelId,
                       String title,
                       String description,
                       String thumbnailDefaultUrl,
                       String thumbnailMediumUrl) {
        this.id = id;
        this.idItem = idItem;
        this.publishedAt = publishedAt;
        this.channelId = channelId;
        this.title = title;
        this.description = description;
        this.thumbnailDefaultUrl = thumbnailDefaultUrl;
        this.thumbnailMediumUrl = thumbnailMediumUrl;
    }
}
