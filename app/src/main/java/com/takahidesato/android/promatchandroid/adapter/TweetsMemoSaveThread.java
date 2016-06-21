package com.takahidesato.android.promatchandroid.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBContentProvider;

/**
 * Created by tsato on 6/20/16.
 */
public class TweetsMemoSaveThread extends Thread {
    Context mContext;
    Handler mHandler;
    int mId;
    String mMemo;

    public TweetsMemoSaveThread(Context context, Handler handler, int id, String memo) {
        mContext = context;
        mHandler = handler;
        mId = id;
        mMemo = memo;
    }

    @Override
    public void run() {
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put(DBColumns.COL_MEMO, mMemo);

        String selection = DBColumns._ID + " = ? ";
        String[] selectionArgs = { String.valueOf(mId) };
        mContext.getContentResolver().update(
                DBContentProvider.Contract.TABLE_TWEETS_FAV.contentUri,
                contentValues,
                selection,
                selectionArgs);
        mHandler.sendMessage(Message.obtain());
    }
}
