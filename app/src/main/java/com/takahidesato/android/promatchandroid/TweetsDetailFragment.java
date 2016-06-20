package com.takahidesato.android.promatchandroid;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.takahidesato.android.promatchandroid.adapter.ObservableScrollView;
import com.takahidesato.android.promatchandroid.adapter.TweetsItemSaveAsync;
import com.takahidesato.android.promatchandroid.adapter.TweetsItem;
import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBContentProvider;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tsato on 4/15/16.
 */
public class TweetsDetailFragment extends Fragment {
    public static final String TAG = TweetsDetailFragment.class.getSimpleName();

    private static boolean sIsFavorite = false;

    @Bind(R.id.osv_container)
    ObservableScrollView mObservableScrollView;
    @Bind(R.id.imv_profile_pic_twitter)
    ImageView mProfileImageView;
    @Bind(R.id.txv_name_twitter)
    TextView mNameTextView;
    @Bind(R.id.txv_screen_name_twitter)
    TextView mScreenNameTextView;
    @Bind(R.id.txv_text_tweet)
    TextView mTweetTextView;
    @Bind(R.id.imv_favorite)
    ImageView mFavoriteImageView;
    @OnClick(R.id.imv_favorite)
    public void onFavoriteClick(View v) {
        if (sIsFavorite) {
            //Log.d(TAG, "item id="+mTweetItem.id+", text="+mTweetItem.text);
            getActivity().getContentResolver().delete(ContentUris.withAppendedId(DBContentProvider.Contract.TABLE_TWEETS_FAV.contentUri, mTweetItem.id), null, null);
        } else {
            //Log.d(TAG, "item id="+mTweetItem.id);
            new TweetsItemSaveAsync(getActivity(), mTweetItem).execute();
        }
        sIsFavorite = !sIsFavorite;
        setFavoriteImageView();
    }
    @OnClick(R.id.imv_share)
    public void onShareClick(View v) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share: idStr = "+mTweetItem.idStr)
                .build());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mTweetItem.text);
        startActivity(intent);
    }

    private TweetsItem mTweetItem;
    private Tracker mTracker;

    public static TweetsDetailFragment getInstance() {
        TweetsDetailFragment fragment = new TweetsDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_tweets, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            sIsFavorite = savedInstanceState.getBoolean("isFavorite");
        }

        setUpLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isFavorite", sIsFavorite);
    }

    public void setUpLayout() {
        mObservableScrollView.setVisibility(View.VISIBLE);

        if (getArguments() != null) {
            mTweetItem = getArguments().getParcelable("item");
        }

        if (mTweetItem == null) {
            mObservableScrollView.setVisibility(View.GONE);
        } else {
            Glide.with(getActivity().getApplicationContext()).load(mTweetItem.profileImageUrl).into(mProfileImageView);
            mNameTextView.setText(mTweetItem.name);
            mScreenNameTextView.setText("@"+mTweetItem.screenName);
            mTweetTextView.setText(mTweetItem.text);

            sIsFavorite = itemExists(mTweetItem.idStr);
        }

        setFavoriteImageView();
    }

    public void setFavoriteImageView() {
        if (sIsFavorite) {
            mFavoriteImageView.setImageResource(R.mipmap.ic_star_black_24dp);
        } else {
            mFavoriteImageView.setImageResource(R.mipmap.ic_star_border_black_24dp);
        }
    }

    private boolean itemExists(String searchItem) {
        String selection = DBColumns.COL_ID_STR + " =?";
        String[] selectionArgs = { searchItem };

        Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.Contract.TABLE_TWEETS_FAV.contentUri, null, selection, selectionArgs, null, null);
        boolean exists = (cursor.getCount() > 0);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(DBColumns.COL_ID_STR)).equals(searchItem)) {
                    mTweetItem.id = cursor.getInt(cursor.getColumnIndex(DBColumns._ID));
                    break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        return exists;
    }
}
