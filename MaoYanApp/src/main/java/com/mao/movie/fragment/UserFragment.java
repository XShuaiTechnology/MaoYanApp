package com.mao.movie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mao.movie.R;
import com.mao.movie.activity.CollectionActivity;
import com.mao.movie.activity.FeedbackActivity;
import com.mao.movie.activity.HistoryActivity;
import com.mao.movie.activity.MainActivity;
import com.mao.movie.activity.SettingActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的Fragment
 */
public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";

    @BindView(R.id.userIconImageView)
    ImageView userIconImageView;
    @BindView(R.id.loginButton)
    Button mLoginButton;
    @BindView(R.id.collectionLayout)
    RelativeLayout mCollectionLayout;
    @BindView(R.id.historyLayout)
    RelativeLayout mHistoryLayout;
    @BindView(R.id.settingLayout)
    RelativeLayout mSettingLayout;
    @BindView(R.id.feedbackLayout)
    RelativeLayout mFeedbackLayout;

    private MainActivity mActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, null);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick({R.id.userIconImageView, R.id.loginButton, R.id.collectionLayout, R.id.historyLayout, R.id.settingLayout, R.id.feedbackLayout})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.userIconImageView:
                break;
            case R.id.loginButton:
                break;
            case R.id.collectionLayout:
                intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.historyLayout:
                intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.settingLayout:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.feedbackLayout:
                intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }


    // ------------------------微信登录相关------------------------------
    private void wxLogout() {
        Log.d(TAG, "wxLogout: ");
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        /**begin invoke umeng api**/
        mActivity.mShareAPI.deleteOauth(mActivity, platform, umdelAuthListener);
    }

    private void wxLogin() {
        Log.d(TAG, "wxLogin: ");
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
        // doOauth接口返回的授权信息
        mActivity.mShareAPI.doOauthVerify(mActivity, platform, umAuthListener);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data != null) {
                Log.d(TAG, "onComplete: platform: " + platform);
                Log.d(TAG, "onComplete: action: " + action);
                Log.d(TAG, "onComplete: data: " + data.toString());
                Toast.makeText(mActivity, "授权成功", Toast.LENGTH_SHORT).show();
                //调用getplatforminfo接口来去除返回值
                mActivity.mShareAPI.getPlatformInfo(mActivity, platform, umAuthMesListener);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(mActivity, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mActivity, "授权取消", Toast.LENGTH_SHORT);
        }
    };

    private UMAuthListener umAuthMesListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Toast.makeText(mActivity, "授权成功", Toast.LENGTH_SHORT);

            String m = share_media.toString();
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                //再次遍历拿出你需要的参数
                Log.d(TAG, "onComplete: entry: " + entry.toString());
                // unionid=oGfXpwD9TnQStXhPeR-0aY1rQ48Q
                // province=山东
                // gender=1
                // screen_name=Gao
                // openid=oCDCXwzpEnl_gbQKTQutfptDRdoI
                // language=zh_CN
                // profile_image_url=http://wx.qlogo.cn/mmopen/Q3auHgzwzM42gaUERJgrialk5Dg8JHqYsTlZ4Tof70Oibx0MxLNhISxzNmobyF19G6bzE4XxQoIORAmiaoZu3iacuw/0
                // country=中国
                // city=日照
                // TODO: 2016/10/29 加载头像 
//                Glide.with(getActivity()).load().in
            }


        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    /**
     * delauth callback interface
     **/
    private UMAuthListener umdelAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(mActivity, "delete Authorize succeed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(mActivity, "delete Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mActivity, "delete Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

}
