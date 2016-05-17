package com.takahidesato.android.promatchandroid.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.takahidesato.android.promatchandroid.R;
import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBContentProvider;

/**
 * Created by tsato on 5/17/16.
 */
public class TweetsAsync extends AsyncTask<String, Void, String> {
    public static final String TAG = TweetsAsync.class.getSimpleName();

    private Context mContext;
    private TweetsItem mTweetsItem;
    private ContentValues mContentValues;

    public TweetsAsync(Context context, TweetsItem tweetItem) {
        mContext = context;
        mTweetsItem = tweetItem;
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public String doInBackground(String... args) {
        mContentValues = new ContentValues();
        mContentValues.clear();
        mContentValues.put(DBColumns.COL_ID_STR, mTweetsItem.idStr);
        mContentValues.put(DBColumns.COL_CREATED_AT, mTweetsItem.createdAt);
        mContentValues.put(DBColumns.COL_TEXT, mTweetsItem.text);
        mContentValues.put(DBColumns.COL_NAME, mTweetsItem.name);
        mContentValues.put(DBColumns.COL_SCREEN_NAME, mTweetsItem.screenName);
        mContentValues.put(DBColumns.COL_PROFILE_IMAGE_URL, mTweetsItem.profileImageUrl);
        mContentValues.put(DBColumns.COL_MEDIA_IMAGE_URL, mTweetsItem.mediaImageUrl);
        mContext.getContentResolver().insert(DBContentProvider.Contract.TABLE_TWEETS.contentUri, mContentValues);
        return "";
    }

    @Override
    public void onPostExecute(String result) {
        Toast.makeText(mContext, R.string.item_was_saved, Toast.LENGTH_SHORT).show();
    }
}
