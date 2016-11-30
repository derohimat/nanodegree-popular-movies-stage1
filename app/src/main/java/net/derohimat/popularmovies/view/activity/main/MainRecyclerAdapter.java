package net.derohimat.popularmovies.view.activity.main;

import android.content.Context;
import android.view.ViewGroup;

import net.derohimat.baseapp.ui.adapter.BaseRecyclerAdapter;
import net.derohimat.popularmovies.R;
import net.derohimat.popularmovies.model.MovieDao;


/**
 * Created by deni rohimat on 17/02/15.
 */
class MainRecyclerAdapter extends BaseRecyclerAdapter<MovieDao, MainlHolder> {

    MainRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.main_row;
    }

    @Override
    public MainlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainlHolder(mContext, getView(parent, viewType), mItemClickListener, mLongItemClickListener);
    }

}
