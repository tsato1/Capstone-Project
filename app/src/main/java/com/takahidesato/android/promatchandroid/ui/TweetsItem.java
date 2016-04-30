package com.takahidesato.android.promatchandroid.ui;

/**
 * Created by tsato on 4/26/16.
 */
public class TweetsItem {
    public String id; // for this app
    public String idStr; // made by twitter
    public String createdAt;
    public String text;
    public String name;
    public String screenName;
    public String profileImageUrl;
    public String mediaImageUrl;

    public TweetsItem(
        String id,
        String idStr,
        String createdAt,
        String text,
        String name,
        String screenName,
        String profileImageUrl,
        String mediaImageUrl
    ) {
        this.id = id;
        this.idStr = idStr;
        this.createdAt = createdAt;
        this.text = text;
        this.name = name;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
        this.mediaImageUrl = mediaImageUrl;
    }
}
