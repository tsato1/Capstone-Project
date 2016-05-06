package com.takahidesato.android.promatchandroid.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.takahidesato.android.promatchandroid.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tsato on 4/21/16.
 */
public class TweetsRecyclerAdapter extends RecyclerView.Adapter<TweetsRecyclerAdapter.TweetsViewHolder> {
    private final static int KEY = 1;

    private Context mContext;
    private List<TweetsItem> mTweetsList;
    private OnCardItemClickListener mOnCardItemClickListener;

    public TweetsRecyclerAdapter(Context context, List<TweetsItem> list) {
        mContext = context;
        mTweetsList = list;
    }

    @Override
    public TweetsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_tweets, viewGroup, false);
        TweetsViewHolder viewHolder = new TweetsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetsViewHolder viewHolder, int i) {
        TweetsItem item = mTweetsList.get(i);

        Glide.with(mContext)
                .load(item.profileImageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.profileImageView);
        viewHolder.screenNameTextView.setText(item.screenName);
        viewHolder.tweetTextView.setText(item.text);
    }

    @Override
    public int getItemCount() {
        return mTweetsList!=null? mTweetsList.size(): 0;
    }

    class TweetsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.imv_profile_pic_tweet)
        ImageView profileImageView;
        @Bind(R.id.txv_screen_name)
        TextView screenNameTextView;
        @Bind(R.id.txv_tweet)
        TextView tweetTextView;

        TweetsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnCardItemClickListener.onCardItemClick(getAdapterPosition());
        }
    }

    public interface OnCardItemClickListener {
        void onCardItemClick(int position);
    }

    public void setOnCardItemClickListener(final OnCardItemClickListener onCardItemClickListener) {
        mOnCardItemClickListener = onCardItemClickListener;
    }
}
