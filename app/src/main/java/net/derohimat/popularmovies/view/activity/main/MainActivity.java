package net.derohimat.popularmovies.view.activity.main;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import net.derohimat.baseapp.ui.view.BaseRecyclerView;
import net.derohimat.popularmovies.R;
import net.derohimat.popularmovies.events.MessagesEvent;
import net.derohimat.popularmovies.model.DiscoverMovieApiDao;
import net.derohimat.popularmovies.model.MovieDao;
import net.derohimat.popularmovies.util.Constant;
import net.derohimat.popularmovies.util.DialogFactory;
import net.derohimat.popularmovies.view.AppBaseActivity;
import net.derohimat.popularmovies.view.fragment.detail.DetailFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends AppBaseActivity implements MainMvpView {

    @Bind(R.id.recyclerview)
    BaseRecyclerView mRecyclerView;
    private ProgressBar mProgressBar = null;
    private MainPresenter mPresenter;
    private MainRecyclerAdapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private final static int MAX_WIDTH_COL_DP = 185;
    private String mSort = Constant.SORT_POPULAR;

    @Inject
    EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.main_activity;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        getBaseActionBar().setElevation(0);

        getBaseFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getBaseActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getBaseActionBar().setTitle(getString(R.string.app_name));
                getBaseActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });
        setUpPresenter();
        setUpAdapter();
        setUpRecyclerView();
    }

    @Override
    public void setUpPresenter() {
        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);
        mPresenter.discoverMovies(mSort);
    }

    @Override
    public void setUpAdapter() {
        mAdapter = new MainRecyclerAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            MovieDao selectedItem = mAdapter.getDatas().get(position - 1);

            getBaseActionBar().setTitle(selectedItem.getTitle());
            getBaseFragmentManager().beginTransaction().replace(R.id.container_rellayout,
                    DetailFragment.newInstance(selectedItem)).addToBackStack(null).commit();
        });
    }

    @Override
    public void setUpRecyclerView() {
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        //change span dynamically based on screen width
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        int viewWidth = mRecyclerView.getMeasuredWidth();
                        float cardViewWidth = MAX_WIDTH_COL_DP * getResources().getDisplayMetrics().density;
                        int newSpanCount = Math.max(2, (int) Math.floor(viewWidth / cardViewWidth));
                        mLayoutManager.setSpanCount(newSpanCount);
                        mLayoutManager.requestLayout();
                    }
                });

        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.discoverMovies(mSort);
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

//    @OnClick(R.id.button_main_next_days)
//    void onClick_button_main_next_days() {
//        getBaseActionBar().setTitle("Next days");
//        getBaseFragmentManager().beginTransaction().replace(R.id.container_rellayout, DetailFragment.newInstance(1)).addToBackStack(null).commit();
//    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                showSort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(MessagesEvent event) {
        if (event.ismSuccess()) {
            DialogFactory.createSimpleOkDialog(mContext, getString(R.string.app_name), event.getMessage()).show();
        } else {
            DialogFactory.showErrorSnackBar(mContext, findViewById(android.R.id.content), new Throwable(event.getMessage())).show();
        }
    }

    @Override
    public void showDiscoverMovie(DiscoverMovieApiDao data) {
        mRecyclerView.refreshComplete();
        mAdapter.addAll(data.getResults());
    }

    @Override
    public void showProgress() {
        if (mProgressBar == null) {
            mProgressBar = DialogFactory.DProgressBar(mContext);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSort() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.main_sort_by)
                .setSingleChoiceItems(
                        new String[]{getString(R.string.main_sort_most_popular),
                                getString(R.string.main_sort_highest_rated)},
                        (mSort.equals(Constant.SORT_POPULAR)) ? 0 : 1,
                        (dialog, which) -> {
                            mSort = (which == 0) ? Constant.SORT_POPULAR : Constant.SORT_HIGHEST_RATED;
                            mPresenter.discoverMovies(mSort);
                            dialog.dismiss();
                        });
        builder.create().show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
