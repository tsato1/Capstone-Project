package com.takahidesato.android.promatchandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tsato on 5/7/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL(
                    "CREATE TABLE " + DBColumns.TABLE_SUCCESS + " ("
                    + DBColumns._ID + " INTEGER PRIMARY KEY,"
                    + DBColumns.COL_ID_ITEM + " TEXT NOT NULL,"
                    + DBColumns.COL_PUBLISHED_AT + " TEXT NOT NULL,"
                    + DBColumns.COL_CHANNEL_ID + " TEXT NOT NULL,"
                    + DBColumns.COL_TITLE + " TEXT NOT NULL,"
                    + DBColumns.COL_DESCRIPTION + " TEXT NOT NULL,"
                    + DBColumns.COL_THUMBNAIL_DEFAULT_URL + " TEXT NOT NULL,"
                    + DBColumns.COL_THUMBNAIL_MEDIUM_URL + " TEXT NOT NULL);"
            );
            sqLiteDatabase.execSQL(
                    "CREATE TABLE " + DBColumns.TABLE_TWEETS + " ("
                    + DBColumns._ID + " INTEGER PRIMARY KEY,"
                    + DBColumns.COL_ID_STR + " TEXT NOT NULL,"
                    + DBColumns.COL_CREATED_AT + " TEXT NOT NULL,"
                    + DBColumns.COL_TEXT + " TEXT NOT NULL,"
                    + DBColumns.COL_NAME + " TEXT NOT NULL,"
                    + DBColumns.COL_SCREEN_NAME + " TEXT NOT NULL,"
                    + DBColumns.COL_PROFILE_IMAGE_URL + " TEXT NOT NULL,"
                    + DBColumns.COL_MEDIA_IMAGE_URL + " TEXT NOT NULL);"
            );
//            sqLiteDatabase.execSQL("CREATE TABLE " + DBColumns.TABLE_SUCCESS + " ("
//                    + DBColumns.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + DBColumns.COL_NAME + " TEXT NOT NULL,"
//                    + DBColumns.COL_VIDEO + " TEXT NOT NULL);"
//            );
            sqLiteDatabase.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE_SUCCESS IF EXISTS " + DBColumns.TABLE_SUCCESS);
        sqLiteDatabase.execSQL("DROP TABLE_TWEETS IF EXISTS " + DBColumns.TABLE_TWEETS);
//        sqLiteDatabase.execSQL("DROP TABLE_VIDEO IF EXISTS " + DBColumns.TABLE_SUCCESS);
        onCreate(sqLiteDatabase);
    }
}
