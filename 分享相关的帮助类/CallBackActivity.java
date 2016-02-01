package cn.indeo.cloudTV.customer.ui.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;

import cn.indeo.cloudTV.customer.Event.LogoutEvent;
import cn.indeo.cloudTV.customer.Event.NullMessageEvent;
import cn.indeo.cloudTV.customer.Event.ShareSucceedEvent;
import cn.indeo.cloudTV.customer.R;
import cn.indeo.cloudTV.customer.config.Const;
import cn.indeo.cloudTV.customer.data.ShareData;
import cn.indeo.cloudTV.customer.widget.WeiBo.WeiboShareHelper;
import cn.indeo.cloudTV.customer.wxapi.BaseUIListener;
import customer.app_base.LogUtil;
import customer.app_base.ToastUtil;

public class CallBackActivity extends Activity implements IWeiboHandler.Response, Const {

    private IWeiboShareAPI mWeiboShareAPI;
    private WeiboShareHelper mWeiboShareHelper;
    public static int topicId;
    private boolean isOnNewIntent;
    private boolean isOnCreate;
    public static boolean ISSUCCEED = false;
    public static boolean isShare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        String pic = getIntent().getStringExtra(PICTURE);
        String url = getIntent().getStringExtra(URL);
        String content = getIntent().getStringExtra(Content);
        mWeiboShareHelper = new WeiboShareHelper(this);
        if (!TextUtils.isEmpty(url)) {
            if (!TextUtils.isEmpty(pic) && !TextUtils.isEmpty(content)) {
                if (pic.startsWith("http://") || pic.startsWith("https//")) {
                    mWeiboShareHelper.shareToWeibo(pic, url, content);
                } else {
                    mWeiboShareHelper.shareToWeiboNative(content, pic, url);
                }
            } else if (TextUtils.isEmpty(pic) && !TextUtils.isEmpty(content)) {
                mWeiboShareHelper.shareToWeiboNative(content, null, url);
            } else if (!TextUtils.isEmpty(pic) && TextUtils.isEmpty(content)) {
                if (pic.startsWith("http://") || pic.startsWith("https//")) {
                    mWeiboShareHelper.shareToWeibo(pic, url, null);
                } else {
                    mWeiboShareHelper.shareToWeiboNative(null, pic, url);
                }
            }
        }
        BUS.register(this);
        isOnCreate = true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI = mWeiboShareHelper.getmWeiboShareAPI();
        if (mWeiboShareAPI != null) {
            mWeiboShareAPI.handleWeiboResponse(intent, this);
        }
        isOnNewIntent = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnCreate) {
            isOnCreate = false;
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BUS.unregister(this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                BUS.post(new ShareSucceedEvent(new ShareData(topicId, 1)));
                ToastUtil.showMessage("分享成功");
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                ToastUtil.showMessage("取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                ToastUtil.showMessage("分享失败");
                break;
        }
        ISSUCCEED = true;
        if (BaseUIListener.isShare) {
            //TODO 发消息给PublishActivity去进行qq分享
            BUS.post(new NullMessageEvent(true));
        }
        finish();
    }
}