package cn.indeo.cloudTV.customer.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.open.utils.Util;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

import cn.indeo.cloudTV.customer.R;
import cn.indeo.cloudTV.customer.config.Const;
import customer.app_base.LogUtil;

public class ShareQQActivity extends Activity implements Const {
    private static int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share_qq);
        ArrayList<String> pic = getIntent().getStringArrayListExtra(PICTURE);
        String url = getIntent().getStringExtra(URL);
        String content = getIntent().getStringExtra(Content);
        if (!TextUtils.isEmpty(content) && pic==null) {
            shareToQQKJ(this, url, "分享", null, content);
        } else if (TextUtils.isEmpty(content) && pic.size() > 0) {
            shareToQQKJ(this, url, "分享", pic, null);
        } else if (!TextUtils.isEmpty(content) && pic.size() > 0) {
            shareToQQKJ(this, url, "分享", pic, content);
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
        Tencent tencent = Tencent.createInstance(Const.QQ_APP_ID, activity);
        if (tencent != null) {
            Bundle bundle = new Bundle();

            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
            if (!TextUtils.isEmpty(summary)) {
                bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
            } else {
                bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, null);
            }
            if (imgUrl != null) {
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
            } else {
                ArrayList list=new ArrayList();
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
            }
            if (shareType == QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT) {
                tencent.shareToQzone(activity, bundle, new BaseUIListener());
            } else {
                tencent.publishToQzone(activity, bundle, new BaseUIListener());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUIListener());
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            if (resultCode == Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, new BaseUIListener());
            }
        }
        finish();
    }
}
