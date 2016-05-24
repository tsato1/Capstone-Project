package com.takahidesato.android.promatchandroid.database;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by tsato on 5/14/16.
 */
public class DBSuccessLoader extends CursorLoader {
    private DBSuccessLoader(Context context, Uri uri) {
        super(context, uri, Query.SUCCESS, null, null, null);
    }

    public static DBSuccessLoader newInstanceForAll(Context context) {
        return new DBSuccessLoader(context, DBContentProvider.Contract.TABLE_SUCCESS.contentUri);
    }

    public interface Query {
        String[] SUCCESS = {
                DBColumns._ID,
                DBColumns.COL_ID_ITEM,
                DBColumns.COL_PUBLISHED_AT,
                DBColumns.COL_CHANNEL_ID,
                DBColumns.COL_TITLE,
                DBColumns.COL_DESCRIPTION,
                DBColumns.COL_THUMBNAIL_DEFAULT_URL,
                DBColumns.COL_THUMBNAIL_MEDIUM_URL,
                DBColumns.COL_THUMBNAIL_HIGH_URL,
                DBColumns.COL_VIDEO_ID,
        };
    }
}
