package cn.indeo.cloudTV.customer.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.indeo.cloudTV.customer.config.Const;
import cn.indeo.cloudTV.customer.data.User;
import cn.indeo.cloudTV.customer.manager.UserManager;
import cn.indeo.cloudTV.customer.ui.base.BaseActivity;
import customer.app_base.task.TaskCallback;

/**
 * Created by caorui on 2015/9/8.
 */
public class TencentHelper {
    public Tencent mTencent;
    private WeakReference<BaseActivity> mActivity;
    private UserInfo mInfo;

    public TencentHelper(BaseActivity activity) {
        mActivity = new WeakReference<BaseActivity>(activity);
    }

    public void setActivity(BaseActivity activity) {
        mActivity = new WeakReference<BaseActivity>(activity);
    }

    private IUiListener mLoginListener = new BaseUIListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };

    public void loginQQ() {
        if (mTencent == null)
            mTencent = Tencent.createInstance(Const.QQ_APP_ID, mActivity.get().getApplicationContext());

        if (!mTencent.isSessionValid()) {
            mTencent.login(mActivity.get(), "all", mLoginListener);
        }
    }

    public static boolean isQQSupportShare(Context context) {
        return Util.isMobileQQSupportShare(context);
    }

    /**
     * * QQ分享 * *
     *
     * @param targetUrl 这条分享消息被好友点击后的跳转URL
     * @param title     这条分享消息被好友点击后的跳转URL，注：title、imgUrl、summary不能全为空，最少必须有一个是有值的。
     * @param imgUrl    分享的图片URL
     * @param summary   分享的消息摘要，最长50个字
     */
    public static void shareToQQ(Activity activity, String targetUrl, String title, String imgUrl, String summary) {
        Tencent tencent = Tencent.createInstance(Const.QQ_APP_ID, activity.getApplicationContext());
        if (tencent != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            if (targetUrl != null) {
                bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
            }
            if (title != null) {
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            }
            if (imgUrl != null) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
            }
            if (summary != null) {
                bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
            }
            tencent.shareToQQ(activity, bundle, new BaseUIListener());
        }
    }
    //QQ空间分享
    public static void shareToQQKJ(Activity activity, String targetUrl, String title, ArrayList<String> imgUrl, String summary) {
        if (targetUrl == null || title == null || summary == null || imgUrl == null) {
            return;
        }
        Tencent tencent = Tencent.createInstance(Const.QQ_APP_ID, activity);
        if (tencent != null) {
            Bundle bundle = new Bundle();

            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);

            if (imgUrl != null) {
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
            }
            tencent.shareToQzone(activity, bundle, new BaseUIListener());
        }
    }

    public static void logoutQQ(Activity activity) {
        Tencent tencent = Tencent.createInstance(Const.QQ_APP_ID, activity.getApplicationContext());
        if (tencent != null && tencent.isSessionValid()) {
            tencent.logout(activity);
        }
    }

    public void onQQActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null) {
            if (requestCode == Constants.REQUEST_LOGIN ||
                    requestCode == Constants.REQUEST_APPBAR) {
                mTencent.onActivityResultData(requestCode, resultCode, data, mLoginListener);
            }
        }
//        if (mTencent != null) {
//            mTencent.onActivityResultData(requestCode, resultCode, data, mLoginListener);
//            if (requestCode == Constants.REQUEST_API) {
//                if (resultCode == Constants.RESULT_LOGIN) {
//                    mTencent.handleLoginData(data, mLoginListener);
//                }
//
//            } else if (requestCode == Constants.REQUEST_APPBAR) { //app内应用吧登录
//                if (resultCode == Constants.RESULT_LOGIN) {
//                    ToastUtil.showMessage("应用内登录！");
//                }
//            }
//        }
    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);

            if (!TextUtils.isEmpty(token)
                    && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new BaseUIListener() {
                @Override
                protected void doComplete(JSONObject jsonObject) {
                    try {
                        String name = jsonObject.getString("nickname");
                        String headUrl = jsonObject.getString("figureurl_qq_2");
                        if (headUrl == null || headUrl.length() == 0) {
                            headUrl = jsonObject.getString("figureurl_qq_1");
                        }

                        UserManager.loginThirdparty(mTencent.getOpenId(), name, 2, headUrl, new TaskCallback<Pair<Boolean, String>>() {
                            @Override
                            public void onResult(Pair<Boolean, String> result) {
                                if (result.first) {
                                    mActivity.get().loginSuccess();
                                } else {
                                    mActivity.get().loginFail(result.second);
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            mInfo = new UserInfo(mActivity.get(), mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        }
    }
}
