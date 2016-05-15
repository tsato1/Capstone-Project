package com.takahidesato.android.promatchandroid.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tsato on 4/26/16.
 */
public class SuccessItem implements Parcelable {
    public int id; // for this app
    public String idItem; // made by YouTube
    public String publishedAt;
    public String channelId;
    public String title;
    public String description;
    public String thumbnailDefaultUrl;
    public String thumbnailMediumUrl;

    public SuccessItem(int id,
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

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SuccessItem createFromParcel(Parcel in) {
            return new SuccessItem(in);
        }

        public SuccessItem[] newArray(int size) {
            return new SuccessItem[size];
        }
    };

    private SuccessItem(Parcel in) {
        this.id = in.readInt();
        this.idItem = in.readString();
        this.publishedAt = in.readString();
        this.channelId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.thumbnailDefaultUrl = in.readString();
        this.thumbnailMediumUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(this.id);
        out.writeString(this.idItem);
        out.writeString(this.publishedAt);
        out.writeString(this.channelId);
        out.writeString(this.title);
        out.writeString(this.description);
        out.writeString(this.thumbnailDefaultUrl);
        out.writeString(this.thumbnailMediumUrl);
    }
}
