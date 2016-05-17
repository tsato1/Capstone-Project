package com.takahidesato.android.promatchandroid;

import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.takahidesato.android.promatchandroid.adapter.TweetsAsync;
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
            getActivity().getContentResolver().delete(ContentUris.withAppendedId(DBContentProvider.Contract.TABLE_TWEETS.contentUri, mTweetItem.id), null, null);
        } else {
            Log.d(TAG, "item name="+mTweetItem.name);
            new TweetsAsync(getActivity(), mTweetItem).execute();
        }
        sIsFavorite = !sIsFavorite;
        setFavoriteImageView();
    }

    private TweetsItem mTweetItem;

    public static TweetsDetailFragment getInstance() {
        TweetsDetailFragment fragment = new TweetsDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        setUpLayout();
    }

    public void setUpLayout() {
        if (getArguments() != null) {
            mTweetItem = getArguments().getParcelable("item");
        }

        if (mTweetItem == null) {

        } else {
            Glide.with(getActivity().getApplicationContext()).load(mTweetItem.profileImageUrl).into(mProfileImageView);
            mNameTextView.setText(mTweetItem.name);
            mScreenNameTextView.setText(mTweetItem.screenName);
            mTweetTextView.setText(mTweetItem.text);

            sIsFavorite = itemExists(mTweetItem.idStr);
        }
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

        Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.Contract.TABLE_TWEETS.contentUri, null, selection, selectionArgs, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
