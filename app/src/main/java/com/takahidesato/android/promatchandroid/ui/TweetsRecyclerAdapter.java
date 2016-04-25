package com.takahidesato.android.promatchandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.takahidesato.android.promatchandroid.DetailActivity;
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
    private List<TweetItem> mTweetsList;

    public TweetsRecyclerAdapter(Context context, List<TweetItem> list) {
        mContext = context;
        mTweetsList = list;
    }

    @Override
    public TweetsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_tweets, null);
        TweetsViewHolder viewHolder = new TweetsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(DetailActivity.FRAGMENT_KEY, KEY);
                mContext.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetsViewHolder viewHolder, int i) {
        TweetItem item = mTweetsList.get(i);

        viewHolder.screenNameTextView.setText(item.id_str);
        viewHolder.tweetTextView.setText(item.text);
    }

    @Override
    public int getItemCount() {
        return mTweetsList!=null? mTweetsList.size(): 0;
    }

    public synchronized void refresAdapter(List<TweetItem> items) {
        mTweetsList.clear();
        mTweetsList.addAll(items);
        notifyDataSetChanged();
    }

    static class TweetsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txv_screen_name)
        TextView screenNameTextView;
        @Bind(R.id.txv_tweet)
        TextView tweetTextView;

        TweetsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
