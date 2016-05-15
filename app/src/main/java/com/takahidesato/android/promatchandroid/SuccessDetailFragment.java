package com.takahidesato.android.promatchandroid;

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
    @Bind(R.id.imv_favorite)
    ImageView mFavoriteImageView;
    @OnClick(R.id.imv_favorite)
    public void onFavoriteClick(View v) {
        if (sIsFavorite) {
            // todo are you sure you want unfavorite this item?
        } else {
            new SuccessAsync(getActivity(), mSuccessItem).execute();
        }
        sIsFavorite = !sIsFavorite;
        setFavoriteImageView();
    }
    @Bind(R.id.imv_share)
    ImageView mShareImageView;
    @OnClick(R.id.imv_share)
    public void onShareClick(View v) {

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
        if (getArguments() != null) {
            mSuccessItem = getArguments().getParcelable("item");
        }

        if (mSuccessItem == null) {

        } else {
            String url = mSuccessItem.thumbnailMediumUrl;
            Glide.with(getContext()).load(url).into(mSuccessImageView);

            mSuccessTitleTextView.setText(mSuccessItem.title);
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
}
