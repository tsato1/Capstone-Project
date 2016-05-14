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
public class SuccessRecyclerAdapter extends RecyclerView.Adapter<SuccessRecyclerAdapter.SuccessStoriesViewHolder> {
    private final static String TAG = SuccessRecyclerAdapter.class.getSimpleName();

    private final static int KEY = 0; // = SuccessDetailFragment

    private Context mContext;
    private List<SuccessItem> mSuccessList;
    private OnCardItemClickListener mOnCardItemClickListener;

    public SuccessRecyclerAdapter(Context context, List<SuccessItem> list) {
        mContext = context;
        mSuccessList = list;
    }

    @Override
    public SuccessStoriesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_success, viewGroup, false);
        SuccessStoriesViewHolder viewHolder = new SuccessStoriesViewHolder(view);
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

    public class SuccessStoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.imv_thumbnail_success)
        ImageView thumbnailImageView;
        @Bind(R.id.txv_title_success)
        TextView titleTextView;
        @Bind(R.id.txv_description_success)
        TextView descriptionTextView;

        public SuccessStoriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mContext = view.getContext();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnCardItemClickListener != null) {
                mOnCardItemClickListener.onCardItemClick(getAdapterPosition());
            }
        }
    }

    public interface OnCardItemClickListener {
        void onCardItemClick(int position);
    }

    public void setOnCardItemClickListener(final OnCardItemClickListener onCardItemClickListener) {
        mOnCardItemClickListener = onCardItemClickListener;
    }
}
