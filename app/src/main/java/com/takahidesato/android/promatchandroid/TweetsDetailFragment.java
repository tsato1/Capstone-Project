package com.takahidesato.android.promatchandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.takahidesato.android.promatchandroid.ui.TweetsItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tsato on 4/15/16.
 */
public class TweetsDetailFragment extends Fragment {
    public static final String TAG = TweetsDetailFragment.class.getSimpleName();

    @Bind(R.id.imv_profile_pic_twitter)
    ImageView mProfileImageView;
    @Bind(R.id.txv_name_twitter)
    TextView mNameTextView;
    @Bind(R.id.txv_screen_name_twitter)
    TextView mScreenNameTextView;
    @Bind(R.id.txv_text_tweet)
    TextView mTweetTextView;

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
            Glide.with(getContext()).load(mTweetItem.profileImageUrl).into(mProfileImageView);
            mNameTextView.setText(mTweetItem.name);
            mScreenNameTextView.setText(mTweetItem.screenName);
            mTweetTextView.setText(mTweetItem.text);
        }
    }

}
