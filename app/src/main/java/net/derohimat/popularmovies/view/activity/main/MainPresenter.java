package net.derohimat.popularmovies.view.activity.main;

import android.content.Context;

import net.derohimat.baseapp.presenter.BasePresenter;
import net.derohimat.popularmovies.BaseApplication;
import net.derohimat.popularmovies.R;
import net.derohimat.popularmovies.data.remote.APIService;
import net.derohimat.popularmovies.events.MessagesEvent;
import net.derohimat.popularmovies.model.DiscoverMovieApiDao;
import net.derohimat.popularmovies.util.Constant;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MainPresenter implements BasePresenter<MainMvpView> {

    @Inject
    MainPresenter(Context context) {
        ((BaseApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Inject
    APIService mAPIService;
    @Inject
    EventBus mEventBus;

    private MainMvpView mMainMvpView;
    private Subscription mSubscription;
    private DiscoverMovieApiDao mWeatherPojo;

    @Override
    public void attachView(MainMvpView view) {
        mMainMvpView = view;
    }

    @Override
    public void detachView() {
        mMainMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    void discoverMovies(String sortBy) {

        mMainMvpView.showProgress();
        if (mSubscription != null) mSubscription.unsubscribe();

        BaseApplication baseApplication = BaseApplication.get(mMainMvpView.getContext());

        mSubscription = mAPIService.discoverMovie(Constant.MOVIEDB_APIKEY, sortBy)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(baseApplication.getSubscribeScheduler())
                .subscribe(new Subscriber<DiscoverMovieApiDao>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("Movies loaded " + mWeatherPojo);
                        mMainMvpView.showDiscoverMovie(mWeatherPojo);
                        mMainMvpView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable error) {
                        Timber.e("Error loading Movies", error);
                        if (isHttp404(error)) {
                            mEventBus.post(new MessagesEvent(false, baseApplication.getString(R.string.error_not_found)));
                        } else {
                            mEventBus.post(new MessagesEvent(false, baseApplication.getString(R.string.error_loading_movie)));
                        }

                        mMainMvpView.hideProgress();
                    }

                    @Override
                    public void onNext(DiscoverMovieApiDao weatherPojo) {
                        mWeatherPojo = weatherPojo;
                    }
                });
    }

    private static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }
}