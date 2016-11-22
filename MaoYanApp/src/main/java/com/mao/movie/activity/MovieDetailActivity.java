package com.mao.movie.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mao.movie.R;
import com.mao.movie.model.RecommendMovie;
import com.mao.movie.ui.ijkplayer.JCVideoPlayerCustom;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GaoMatrix on 2016/11/22.
 */
public class MovieDetailActivity extends BaseActivity {
    public static final String MOVIE_DATA = "movie_data";

    RecommendMovie.RowsBean mMovie;
    @BindView(R.id.videoPlayer)
    JCVideoPlayerCustom mVideoPlayer;
    @BindView(R.id.coverImageView)
    ImageView mCoverImageView;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.labelTextView)
    TextView mLabelTextView;
    @BindView(R.id.directorTextView)
    TextView mDirectorTextView;
    @BindView(R.id.actorTextView)
    TextView mActorTextView;
    @BindView(R.id.ratingTextView)
    TextView mRatingTextView;
    @BindView(R.id.descriptionTextView)
    TextView mDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        initData();

    }

    private void initData() {
        mMovie = (RecommendMovie.RowsBean) getIntent().getSerializableExtra(MOVIE_DATA);
        if (mMovie != null) {
            Glide.with(this).load(mMovie.getUrl()).into(mCoverImageView);
            mTitleTextView.setText(mMovie.getName());
            //mDirectorTextView.setText(mMovie.get);
            mLabelTextView.setText(mMovie.getLabel());
            mDescriptionTextView.setText(mMovie.getDescription());
            mRatingTextView.setText(mMovie.getRating());
        }
    }

}
