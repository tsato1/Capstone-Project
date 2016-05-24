package com.takahidesato.android.promatchandroid;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.takahidesato.android.promatchandroid.adapter.ObservableScrollView;
import com.takahidesato.android.promatchandroid.adapter.SuccessAsync;
import com.takahidesato.android.promatchandroid.adapter.SuccessItem;
import com.takahidesato.android.promatchandroid.database.DBColumns;
import com.takahidesato.android.promatchandroid.database.DBContentProvider;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tsato on 4/15/16.
 */
public class SuccessDetailFragment extends Fragment {
    public  static final String TAG = SuccessDetailFragment.class.getSimpleName();

    private static final float PARALLAX_FACTOR = 1.25f;

    private static boolean sIsFavorite = false;

    @Bind(R.id.osv_container)
    ObservableScrollView mObservableScrollView;
    @Bind(R.id.frl_success_image_container)
    View mSuccessImageContainer;
    @Bind(R.id.imv_image_success)
    ImageView mSuccessImageView;
    @Bind(R.id.txv_title_success)
    TextView mSuccessTitleTextView;
    @Bind(R.id.txv_date_success)
    TextView mSuccessDateTextView;
    @Bind(R.id.imv_favorite)
    ImageView mFavoriteImageView;
    @Bind(R.id.imv_share)
    ImageView mShareImageView;
    @OnClick(R.id.imv_image_success)
    public void onImageClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + mSuccessItem.videoId));
        intent.putExtra("VIDEO_ID", mSuccessItem.videoId);
        getContext().startActivity(intent);
    }
    @OnClick(R.id.imv_favorite)
    public void onFavoriteClick(View v) {
        if (sIsFavorite) {
            getActivity().getContentResolver().delete(ContentUris.withAppendedId(DBContentProvider.Contract.TABLE_SUCCESS_FAV.contentUri, mSuccessItem.id), null, null);
        } else {
            //Log.d(TAG, "item="+mSuccessItem.title);
            new SuccessAsync(getActivity(), mSuccessItem).execute();
        }
        sIsFavorite = !sIsFavorite;
        setFavoriteImageView();
    }
    @OnClick(R.id.imv_share)
    public void onShareClick(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + mSuccessItem.videoId);
        startActivity(intent);
    }

    private int mScrollY;
    private SuccessItem mSuccessItem;

    public static SuccessDetailFragment getInstance() {
        SuccessDetailFragment fragment = new SuccessDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_success, container, false);
        ButterKnife.bind(this, view);

        mObservableScrollView.setOnScrollChanged(new ObservableScrollView.OnScrollChanged() {
            @Override
            public void onScrollChanged() {
                mScrollY = mObservableScrollView.getScrollY();
                mSuccessImageContainer.setTranslationY((int) (mScrollY - mScrollY / PARALLAX_FACTOR));
            }
        });

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
            mSuccessItem = getArguments().getParcelable("item");
        }

        if (mSuccessItem == null) {
            mObservableScrollView.setVisibility(View.GONE);
        } else {
            String url = mSuccessItem.thumbnailMediumUrl;
            Glide.with(getActivity().getApplicationContext()).load(url).into(mSuccessImageView);

            mSuccessTitleTextView.setText(mSuccessItem.title);

            String date = mSuccessItem.description.substring(mSuccessItem.description.length()-9, mSuccessItem.description.length()-1);
            mSuccessDateTextView.setText(date);

            sIsFavorite = itemExists(String.valueOf(mSuccessItem.title));
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
        String selection = DBColumns.COL_TITLE + " =?";
        String[] selectionArgs = { searchItem };

        Cursor cursor = getActivity().getContentResolver().query(DBContentProvider.Contract.TABLE_SUCCESS_FAV.contentUri, null, selection, selectionArgs, null, null);
        boolean exists = (cursor.getCount() > 0);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(DBColumns.COL_TITLE)).equals(searchItem)) {
                    mSuccessItem.id = cursor.getInt(cursor.getColumnIndex(DBColumns._ID));
                    break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        return exists;
    }
}
