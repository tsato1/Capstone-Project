package com.takahidesato.android.promatchandroid.ui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tsato on 4/26/16.
 */
public class TweetsItem implements Parcelable {
    public int id; // for this app
    public String idStr; // made by twitter
    public String createdAt;
    public String text;
    public String name;
    public String screenName;
    public String profileImageUrl;
    public String mediaImageUrl;

    public TweetsItem(
        int id,
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

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<TweetsItem>() {
        public TweetsItem createFromParcel(Parcel in) {
            return new TweetsItem(in);
        }

        public TweetsItem[] newArray(int size) {
            return new TweetsItem[size];
        }
    };

    private TweetsItem(Parcel in) {
        this.id = in.readInt();
        this.idStr = in.readString();
        this.createdAt = in.readString();
        this.text = in.readString();
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.mediaImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(this.id);
        out.writeString(this.idStr);
        out.writeString(this.createdAt);
        out.writeString(this.text);
        out.writeString(this.name);
        out.writeString(this.screenName);
        out.writeString(this.profileImageUrl);
        out.writeString(this.mediaImageUrl);
    }
}
