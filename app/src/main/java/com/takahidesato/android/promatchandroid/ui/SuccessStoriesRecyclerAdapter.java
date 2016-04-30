package com.takahidesato.android.promatchandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.takahidesato.android.promatchandroid.DetailActivity;
import com.takahidesato.android.promatchandroid.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tsato on 4/17/16.
 */
public class SuccessStoriesRecyclerAdapter extends RecyclerView.Adapter<SuccessStoriesRecyclerAdapter.SuccessStoriesViewHolder> {
    private final static int KEY = 0; // = SuccessDetailFragment

    private Context mContext;
    private List<SuccessItem> mSuccessList;

    public SuccessStoriesRecyclerAdapter(Context context, List<SuccessItem> list) {
        mContext = context;
        mSuccessList = list;
    }

    @Override
    public SuccessStoriesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_success, viewGroup, false);
        SuccessStoriesViewHolder viewHolder = new SuccessStoriesViewHolder(view);
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
    public void onBindViewHolder(SuccessStoriesViewHolder viewHolder, int i) {
        SuccessItem item = mSuccessList.get(i);

        Glide.with(mContext)
                .load(item.thumbnailMediumUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.thumbnailImageView);

        viewHolder.titleTextView.setText(item.title);
        viewHolder.descriptionTextView.setText(item.description);
    }

    @Override
    public int getItemCount() {
        return (null != mSuccessList? mSuccessList.size(): 0);
    }

    static class SuccessStoriesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imv_thumbnail_success)
        ImageView thumbnailImageView;
        @Bind(R.id.txv_title_success)
        TextView titleTextView;
        @Bind(R.id.txv_description_success)
        TextView descriptionTextView;

        public SuccessStoriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
