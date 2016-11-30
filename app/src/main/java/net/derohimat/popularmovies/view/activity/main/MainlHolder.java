package net.derohimat.popularmovies.view.activity.main;

import android.content.Context;
import android.view.View;

import net.derohimat.baseapp.ui.adapter.BaseRecyclerAdapter;
import net.derohimat.baseapp.ui.adapter.viewholder.BaseItemViewHolder;
import net.derohimat.baseapp.ui.view.BaseImageView;
import net.derohimat.popularmovies.R;
import net.derohimat.popularmovies.model.MovieDao;
import net.derohimat.popularmovies.util.Constant;

import butterknife.Bind;


/**
 * Created by deni rohimat on 17/02/15.
 */
class MainlHolder extends BaseItemViewHolder<MovieDao> {

    @Bind(R.id.iv_poster)
    BaseImageView mImgPoster;

    MainlHolder(Context context, View itemView, BaseRecyclerAdapter.OnItemClickListener itemClickListener,
                BaseRecyclerAdapter.OnLongItemClickListener longItemClickListener) {
        super(itemView, itemClickListener, longItemClickListener);
        this.mContext = context;
    }

    @Override
    public void bind(MovieDao item) {
        String imagePath = Constant.ROOT_POSTER_IMAGE_URL + item.getPoster_path();

        mImgPoster.setImageUrl(imagePath);
    }
}
