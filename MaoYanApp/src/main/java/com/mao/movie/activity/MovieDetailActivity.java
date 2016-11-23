package com.mao.movie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.mao.movie.App;
import com.mao.movie.R;
import com.mao.movie.model.BdPcsToken;
import com.mao.movie.model.Defaultcontent;
import com.mao.movie.model.RecommendMovie;
import com.mao.movie.util.DBUtils;
import com.mao.movie.util.VideoAnalysis;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by GaoMatrix on 2016/11/22.
 */
public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_DATA = "movie_data";

    RecommendMovie.RowsBean mMovie;
    @BindView(R.id.videoPlayer)
    JCVideoPlayerStandard mVideoPlayer;
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

    DbUtils db;
    @BindView(R.id.collectLayout)
    LinearLayout mCollectLayout;
    @BindView(R.id.commentLayout)
    LinearLayout mCommentLayout;
    @BindView(R.id.shareLayout)
    LinearLayout mShareLayout;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Map<String, Object> result = (Map<String, Object>) msg.obj;
            String finalpath = (String) result.get("path");
            boolean m3u8 = false;
            try {
                m3u8 = (Boolean) result.get("m3u8");
            } catch (Exception e) {
            }
            if (TextUtils.isEmpty(finalpath)) {
                Toast.makeText(MovieDetailActivity.this, "视频地址解析失败，请确认视频源",
                        Toast.LENGTH_SHORT).show();
                MovieDetailActivity.this.finish();
                return;
            }
            mVideoPlayer.setUp(finalpath, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,
                    mMovie.getName());
            Glide.with(MovieDetailActivity.this)
                    .load(mMovie.getUrl())
                    .into(mVideoPlayer.thumbImageView);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        initData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private void initData() {
        db = DBUtils.getDbUtils(getApplicationContext());
        mMovie = (RecommendMovie.RowsBean) getIntent().getSerializableExtra(MOVIE_DATA);
        if (mMovie != null) {
            Glide.with(this).load(mMovie.getUrl()).into(mCoverImageView);
            mTitleTextView.setText(mMovie.getName());
            //mDirectorTextView.setText(mMovie.get);
            mLabelTextView.setText(mMovie.getLabel());
            mDescriptionTextView.setText(mMovie.getDescription());
            mRatingTextView.setText(mMovie.getRating());
        }

        parseUrl();
    }

    private void parseUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (TextUtils.isEmpty(App.getInstance().analysisUrl)) {
                    Connection connect = Jsoup.connect(VideoAnalysis.ANALYSIS_URL).ignoreContentType(true);
                    try {
                        Document d = connect.timeout(10000).validateTLSCertificates(false).get();
                        if (null != d && null != d.text()) {
                            BdPcsToken bt = new Gson().fromJson(d.text(), BdPcsToken.class);
                            if (null != bt) {
                                App.getInstance().analysisUrl = bt.getApival();
                                try {
                                    db.saveOrUpdate(bt);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (TextUtils.isEmpty(App.getInstance().analysisUrl)) {
                    try {
                        BdPcsToken bt = db.findFirst(Selector.from(BdPcsToken.class).where("apinm", "=",
                                VideoAnalysis.ANALYSIS_KEY));
                        if (null != bt) {
                            App.getInstance().analysisUrl = bt.getApival();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Map<String, Object> result = VideoAnalysis.getVideoPath(mMovie.getIntentData(), MovieDetailActivity.this);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    @OnClick({R.id.collectLayout, R.id.commentLayout, R.id.shareLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collectLayout:
                break;
            case R.id.commentLayout:
                break;
            case R.id.shareLayout:
                new ShareAction(MovieDetailActivity.this).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.MORE)
                        .withTitle("猫眼影院")
                        .withText(mMovie.getName())
                        .withMedia(new UMImage(MovieDetailActivity.this, mMovie.getUrl()))
                        .withTargetUrl("https://wsq.umeng.com/")
                        .setCallback(umShareListener)
                        .open();
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(MovieDetailActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MovieDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MovieDetailActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MovieDetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }
}
