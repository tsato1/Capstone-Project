package com.takahidesato.android.promatchandroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takahidesato.android.promatchandroid.ui.SuccessItem;
import com.takahidesato.android.promatchandroid.ui.SuccessRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tsato on 5/11/16.
 */
public class SuccessListFavoriteFragment extends Fragment implements SuccessRecyclerAdapter.OnCardItemClickListener {
    public static final String TAG = SuccessListFavoriteFragment.class.getSimpleName();

    @Bind(R.id.srl_success)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rcv_success)
    RecyclerView mRecyclerView;

    private SuccessRecyclerAdapter mSuccessRecyclerAdapter = null;
    private List<SuccessItem> mSuccessFavoriteList = new ArrayList<>();
    private boolean mIsDualPane;

    public static Fragment getInstance(int key) {
        Fragment fragment = new SuccessListFavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ViewPagerFragment.FRAGMENT_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_success, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) Log.i(TAG, "Fragment position = " + args.getInt(ViewPagerFragment.FRAGMENT_KEY));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstatnceState) {
        super.onActivityCreated(savedInstatnceState);

        View detailFragment = getActivity().findViewById(R.id.frl_fragment_container);
        mIsDualPane = detailFragment != null && detailFragment.getVisibility() == View.VISIBLE;

        /***** determining column count for staggered grid view *****/
        int columnCount = 1;
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            columnCount = 2;
            /***if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
             columnCount = 3;
             }***/
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                columnCount = 2;
            }
        }

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mSuccessRecyclerAdapter = new SuccessRecyclerAdapter(getContext(), mSuccessFavoriteList);
        mSuccessRecyclerAdapter.setOnCardItemClickListener(this);
        mRecyclerView.setAdapter(mSuccessRecyclerAdapter);

        //todo implement cursor loader
    }

    @Override
    public void onCardItemClick(int position) {
        Log.d(TAG, "onCardItemSelected(): Card Position = " + position);

        if (mIsDualPane) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SuccessDetailFragment fragment = (SuccessDetailFragment) manager.findFragmentByTag(SuccessDetailFragment.TAG);
            Bundle args = fragment.getArguments();
            args.putInt(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_SUCCESS_FAVORITE);
            args.putParcelable("item", mSuccessFavoriteList.get(position));
            fragment.setUpLayout();
        } else {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(ViewPagerFragment.FRAGMENT_KEY, ViewPagerFragment.FRAGMENT_KEY_SUCCESS_FAVORITE);
            intent.putExtra("item", mSuccessFavoriteList.get(position));
            getParentFragment().startActivityForResult(intent, ViewPagerFragment.FRAGMENT_KEY_SUCCESS_FAVORITE);
        }
    }
}
