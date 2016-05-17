package com.takahidesato.android.promatchandroid.database;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by tsato on 5/17/16.
 */
public class DBTweetsLoader extends CursorLoader {
    private DBTweetsLoader(Context context, Uri uri) {
        super(context, uri, Query.TWEETS, null, null, null);
    }

    public static DBTweetsLoader newInstanceForAll(Context context) {
        return new DBTweetsLoader(context, DBContentProvider.Contract.TABLE_TWEETS.contentUri);
    }

    public interface Query {
        String[] TWEETS = {
            DBColumns._ID,
            DBColumns.COL_ID_STR,
            DBColumns.COL_CREATED_AT,
            DBColumns.COL_TEXT,
            DBColumns.COL_NAME,
            DBColumns.COL_SCREEN_NAME,
            DBColumns.COL_PROFILE_IMAGE_URL,
            DBColumns.COL_MEDIA_IMAGE_URL,
        };
    }
}
