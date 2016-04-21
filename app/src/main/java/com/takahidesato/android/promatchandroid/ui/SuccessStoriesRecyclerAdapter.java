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
 * Created by tsato on 4/17/16.
 */
public class SuccessStoriesRecyclerAdapter extends RecyclerView.Adapter<SuccessStoriesRecyclerAdapter.SuccessStoriesViewHolder> {
    Context mContext;
    List<SuccessItem> mList;
    String mUrl;

    public SuccessStoriesRecyclerAdapter(Context context, List<SuccessItem> list, String url) {
        mContext = context;
        mList = list;
        mUrl = url;
    }

    @Override
    public SuccessStoriesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_success, null);

        SuccessStoriesViewHolder viewHolder = new SuccessStoriesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SuccessStoriesViewHolder viewHolder, int i) {
        SuccessItem item = mList.get(i);

        Glide.with(mContext)
                .load(mUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.thumbnailImageView);

        viewHolder.descriptionTextView.setText("Sample text");
    }

    @Override
    public int getItemCount() {
        return (null != mList? mList.size(): 0);
    }

    static class SuccessStoriesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imv_thumbnail_success)
        ImageView thumbnailImageView;
        @Bind(R.id.txv_description_success)
        TextView descriptionTextView;

        public SuccessStoriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
