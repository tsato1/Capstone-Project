package com.takahidesato.android.promatchandroid.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by tsato on 5/6/16.
 * Reference: Udacity course project: XYZReader
 */
public class ObservableScrollView extends ScrollView {
    private OnScrollChanged mOnScrollChanged;

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChanged != null) {
            mOnScrollChanged.onScrollChanged();
        }
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int scrollY = getScrollY();
        if (scrollY > 0 && mOnScrollChanged != null) {
            mOnScrollChanged.onScrollChanged();
        }
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public interface OnScrollChanged {
        void onScrollChanged();
    }

    public void setOnScrollChanged(OnScrollChanged onScrollChanged) {
        mOnScrollChanged = onScrollChanged;
    }
}
