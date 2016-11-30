package net.derohimat.popularmovies.view.fragment.detail;

import net.derohimat.popularmovies.model.MovieDao;
import net.derohimat.popularmovies.view.MvpView;

interface DetailMvpView extends MvpView {

    void showMovie(MovieDao data);

}