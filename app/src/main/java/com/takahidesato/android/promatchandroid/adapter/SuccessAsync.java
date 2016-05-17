package com.takahidesato.android.promatchandroid.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.takahidesato.android.promatchandroid.R;
import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBContentProvider;

/**
 * Created by tsato on 5/13/16.
 */
public class SuccessAsync extends AsyncTask<String, Void, String> {
    public static final String TAG = SuccessAsync.class.getSimpleName();

    private Context mContext;
    private SuccessItem mSuccessItem;
    private ContentValues mContentValues;

    public SuccessAsync(Context context, SuccessItem successItem) {
        mContext = context;
        mSuccessItem = successItem;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public String doInBackground(String... args) {
        mContentValues = new ContentValues();
        mContentValues.clear();
        mContentValues.put(DBColumns.COL_ID_ITEM, mSuccessItem.idItem);
        mContentValues.put(DBColumns.COL_PUBLISHED_AT, mSuccessItem.publishedAt);
        mContentValues.put(DBColumns.COL_CHANNEL_ID, mSuccessItem.channelId);
        mContentValues.put(DBColumns.COL_TITLE, mSuccessItem.title);
        mContentValues.put(DBColumns.COL_DESCRIPTION, mSuccessItem.description);
        mContentValues.put(DBColumns.COL_THUMBNAIL_DEFAULT_URL, mSuccessItem.thumbnailDefaultUrl);
        mContentValues.put(DBColumns.COL_THUMBNAIL_MEDIUM_URL, mSuccessItem.thumbnailMediumUrl);
        mContext.getContentResolver().insert(DBContentProvider.Contract.TABLE_SUCCESS.contentUri, mContentValues);
        return "";
    }

    @Override
    public void onPostExecute(String result) {
        Toast.makeText(mContext, R.string.item_was_saved, Toast.LENGTH_SHORT).show();
    }
}
