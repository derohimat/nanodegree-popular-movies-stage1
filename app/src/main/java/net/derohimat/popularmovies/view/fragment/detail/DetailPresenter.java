package net.derohimat.popularmovies.view.fragment.detail;

import android.content.Context;

import net.derohimat.baseapp.presenter.BasePresenter;
import net.derohimat.popularmovies.BaseApplication;
import net.derohimat.popularmovies.model.MovieDao;

import javax.inject.Inject;

import rx.Subscription;

public class DetailPresenter implements BasePresenter<DetailMvpView> {

    private DetailMvpView mView;
    private Subscription mSubscription;
    private MovieDao mMovieDao;

    @Inject
    DetailPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

//    Just Prepare if Data Not Completed
//    @Inject
//    APIService mAPIService;

    @Override
    public void attachView(DetailMvpView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void loadMovie(MovieDao movieDao) {
        mMovieDao = movieDao;
        mView.showMovie(mMovieDao);
    }
}