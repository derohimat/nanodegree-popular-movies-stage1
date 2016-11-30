package net.derohimat.popularmovies.data.remote;

import net.derohimat.popularmovies.model.DiscoverMovieApiDao;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheMovieService {
    @GET("discover/movie")
    Call<DiscoverMovieApiDao> discoverMovie(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);
}