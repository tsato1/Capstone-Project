package com.takahidesato.android.promatchandroid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takahidesato.android.promatchandroid.network.TwitterServiceGenerator;
import com.takahidesato.android.promatchandroid.network.Util;
import com.takahidesato.android.promatchandroid.network.YouTubeApi;
import com.takahidesato.android.promatchandroid.network.YouTubeResponseBody;
import com.takahidesato.android.promatchandroid.ui.SuccessItem;
import com.takahidesato.android.promatchandroid.ui.SuccessStoriesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tsato on 4/14/16.
 */
public class SuccessStoriesListFragment extends Fragment {
    private static final String TAG = SuccessStoriesListFragment.class.getSimpleName();
    private static final String KEY = "position";

    @Bind(R.id.srl_success)
    SwipeRefreshLayout mSwipeRefreshLayout; // TODO call refresh(false) to stop animation
    @Bind(R.id.rcv_success)
    RecyclerView mRecyclerView;
    //@OnItemClick(R.id.rcv_success)
    //public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    //    Log.d(TAG, "coming here");
//        MovieItem item = (MovieItem) parent.getItemAtPosition(position);
//
//        if (mIsDualPane) {
//            Log.d(MovieListFragment.class.getSimpleName(), "You clicked " + item.title + " at " + position);
//            MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.frag_movie_detail);
//
//            Bundle args = movieDetailFragment.getArguments();
//            args.putParcelable("item", item);
//            args.putInt("indexOfItem", position);
//            args.putInt("pageCode", pageCode);
//            movieDetailFragment.setUpLayout();
//        } else {
//            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
//            if (pageCode == CODE_FAVORITE) item.poster = null;
//            intent.putExtra("item", item);
//            intent.putExtra("indexOfItem", position);
//            intent.putExtra("pageCode", pageCode);
//            startActivityForResult(intent, pageCode);
//        }
    //}

    private SuccessStoriesRecyclerAdapter mSuccessStoriesRecyclerAdapter = null;
    private List<SuccessItem> mSuccessList = new ArrayList<>();

    public static SuccessStoriesListFragment getInstance(int position) {
        SuccessStoriesListFragment fragment = new SuccessStoriesListFragment();
        Bundle args = new Bundle();
        args.putInt(KEY, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_success, container, false);
        Bundle args = getArguments();
        if (args != null) Log.i(TAG, "Fragment position = " + args.getInt(KEY));
        ButterKnife.bind(this, view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
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

        /***** determining column count for staggered grid view *****/
        int columnCount = 1;
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            columnCount = 2;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                columnCount = 3;
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                columnCount = 2;
            }
        }
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mSuccessStoriesRecyclerAdapter = new SuccessStoriesRecyclerAdapter(getContext(), mSuccessList);
        mRecyclerView.setAdapter(mSuccessStoriesRecyclerAdapter);

        //TODO wifi / network check
        retrieveData();
    }

    private void retrieveData() {
        YouTubeApi youTubeApi = TwitterServiceGenerator.createService(YouTubeApi.class, Util.BASE_GOOGLE_URL);

        Call<YouTubeResponseBody> call = youTubeApi.getSuccess(
                Util.PART,
                Util.MAX_RESULTS,
                Util.PLAYLIST_ID,
                Util.KEY);

        call.enqueue(new Callback<YouTubeResponseBody>() {
            @Override
            public void onResponse(Call<YouTubeResponseBody> call, Response<YouTubeResponseBody> response) {
                if (response.code() == 200) {
                    Log.d("Retrofit YouTube", "response.code()="+response.code());

                    YouTubeResponseBody item = response.body();

                    for (int i = 0; i < 5; i++) {
                        Log.d("Retrofit YouTube",
                                "id="+item.items.get(i).id + ", " +
                                "title="+item.items.get(i).snippet.title +", " +
                                "description="+item.items.get(i).snippet.description +", " +
                                "thumbnail default url="+item.items.get(i).snippet.thumbnails.defaultSize.url+", " +
                                "thumbnail medium url="+item.items.get(i).snippet.thumbnails.medium.url);
                    }

                    //mSuccessStoriesRecyclerAdapter.refresAdapter(item.items);
                }
            }

            @Override
            public void onFailure(Call<YouTubeResponseBody> call, Throwable t) {
                Log.e(TAG, "Retrofit YouTube Error: " + t.toString());
            }
        });
    }
}
