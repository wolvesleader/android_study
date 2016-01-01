package com.quincy.keepingcar.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.quincy.keepingcar.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 
 * @author quincy
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI wxApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        wxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        wxApi.handleIntent(getIntent(), this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        int i = 1;
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
            	Toast.makeText(this, "分享成功！", Toast.LENGTH_LONG).show();
                //ToastUtil.showMessage("分享成功！");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
            	Toast.makeText(this, "分享取消！", Toast.LENGTH_LONG).show();
                //ToastUtil.showMessage("分享取消！");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            	Toast.makeText(this, "分享拒绝！", Toast.LENGTH_LONG).show();
               // ToastUtil.showMessage("分享拒绝！");
                break;
        }
        finish();
    }
}
