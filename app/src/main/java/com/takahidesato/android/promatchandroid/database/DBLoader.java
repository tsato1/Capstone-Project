package com.takahidesato.android.promatchandroid.database;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by tsato on 5/14/16.
 */
public class DBLoader extends CursorLoader {
    private DBLoader(Context context, Uri uri) {
        super(context, uri, Query.SUCCESS, null, null, null);
    }

    public static DBLoader newInstanceForAll(Context context, String table) {
        switch (table) {
            case DBColumns.TABLE_SUCCESS:
                return new DBLoader(context, DBContentProvider.Contract.TABLE_SUCCESS.contentUri);
            case DBColumns.TABLE_TWEETS:
                return new DBLoader(context, DBContentProvider.Contract.TABLE_TWEETS.contentUri);
        }
        return null;
    }

//    public static DBLoader newInstanceForItemId(Context context, long id) {
//        return new DBLoader(context, DBContentProvider.Contract.)
//    }

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
        };
    }
}
