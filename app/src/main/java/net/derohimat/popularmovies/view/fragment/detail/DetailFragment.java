package net.derohimat.popularmovies.view.fragment.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import net.derohimat.baseapp.ui.fragment.BaseFragment;
import net.derohimat.baseapp.ui.view.BaseImageView;
import net.derohimat.popularmovies.R;
import net.derohimat.popularmovies.model.MovieDao;
import net.derohimat.popularmovies.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;

public class DetailFragment extends BaseFragment implements DetailMvpView {

    private static final String ARG_DATA = "ARG_DATA";
    @Bind(R.id.iv_backdrop)
    BaseImageView mIvBackdrop;
    @Bind(R.id.iv_poster)
    BaseImageView mIvPoster;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_releasedate)
    TextView mTvReleaseDate;
    @Bind(R.id.tv_voteavg)
    TextView mTvVoteAvg;
    @Bind(R.id.tv_synopsis)
    TextView mTvSynopsis;

    private DetailPresenter mPresenter;
    private MovieDao mMovieDao;

    public static DetailFragment newInstance(MovieDao movieDao) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, movieDao);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.detail_fragment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        mMovieDao = getArguments().getParcelable(ARG_DATA);

        setUpPresenter();

        //for recreation of the toolbar
        setHasOptionsMenu(true);
    }

    private void setUpPresenter() {
        mPresenter = new DetailPresenter(getActivity());
        mPresenter.attachView(this);
        mPresenter.loadMovie(mMovieDao);
    }

    @Override
    public void showMovie(MovieDao data) {
        mTvTitle.setText(data.getTitle());

        mIvBackdrop.setImageUrl(Constant.ROOT_BACKDROP_IMAGE_URL + data.getBackdrop_path());
        mIvPoster.setImageUrl(Constant.ROOT_POSTER_IMAGE_URL + data.getPoster_path());

        //set release date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate;
        try {
            Date dateRelease = sdf.parse(data.getRelease_date());
            formattedDate = DateFormat.format("dd MMM yyyy", dateRelease).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            formattedDate = data.getRelease_date();
        }
        mTvReleaseDate.setText(formattedDate);

        mTvVoteAvg.setText(data.getVote_average() + "/" + "10");
        mTvSynopsis.setText(data.getOverview());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_detail_refresh:
                mPresenter.loadMovie(mMovieDao);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
