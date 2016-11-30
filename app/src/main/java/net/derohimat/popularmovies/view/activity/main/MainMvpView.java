package net.derohimat.popularmovies.view.activity.main;

import net.derohimat.popularmovies.model.DiscoverMovieApiDao;
import net.derohimat.popularmovies.view.MvpView;

interface MainMvpView extends MvpView {


    void setUpPresenter();

    void setUpAdapter();

    void setUpRecyclerView();

    void showDiscoverMovie(DiscoverMovieApiDao discoverMovieApiDao);

    void showProgress();

    void hideProgress();

    void showSort();
}