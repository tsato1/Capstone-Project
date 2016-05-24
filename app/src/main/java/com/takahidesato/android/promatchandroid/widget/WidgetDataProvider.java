package com.takahidesato.android.promatchandroid.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.takahidesato.android.promatchandroid.R;
import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsato on 5/23/16.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    public static final String TAG = WidgetDataProvider.class.getSimpleName();

    List<WidgetItem> mCollections = new ArrayList();

    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt() called");

        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.app_widget_list_item);

        mView.setTextViewText(R.id.txv_screen_name, "@"+mCollections.get(position).screenName);
        mView.setTextViewText(R.id.txv_created_at, mCollections.get(position).createdAt);
        mView.setTextViewText(R.id.txv_tweet, mCollections.get(position).tweet);

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(ProMatchWidgetProvider.ACTION_TOAST);
        final Bundle bundle = new Bundle();
        bundle.putString(ProMatchWidgetProvider.EXTRA_STRING, "list pushed");
        fillInIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

        return mView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        Thread thread = new Thread() {
            public void run() {
                initData();
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void onDataSetChanged() {
        Thread thread = new Thread() {
            public void run() {
                initData();
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    private void initData() {
        Log.d(TAG, "initData() called");
        if (mContext != null) {
            mCollections.clear();

            Cursor cursor = mContext.getContentResolver().query(DBContentProvider.Contract.TABLE_TWEETS_REP.contentUri, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    WidgetItem mWidgetItem = new WidgetItem();
                    mWidgetItem.screenName = cursor.getString(cursor.getColumnIndex(DBColumns.COL_SCREEN_NAME));
                    mWidgetItem.createdAt = cursor.getString(cursor.getColumnIndex(DBColumns.COL_CREATED_AT));
                    mWidgetItem.tweet = cursor.getString(cursor.getColumnIndex(DBColumns.COL_TEXT));
                    mCollections.add(mWidgetItem);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {

    }
}