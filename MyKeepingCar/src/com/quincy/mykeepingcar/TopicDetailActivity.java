package com.quincy.mykeepingcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * Created by Android Studio on 2015/11/12.
 * bbb
 * Author: magic_chen_
 * email:chenyouya56@163.com
 */
public class TopicDetailActivity extends Activity implements View.OnClickListener,IWeiboHandler.Response {


    private WeiboShareHelper mWeiboShareHelper;
   

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWeiboShareHelper != null)
            mWeiboShareHelper.onNewIntent(intent);
       
    }

 

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode){
            case WBConstants.ErrorCode.ERR_OK:
               
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
               
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
               
                break;
        }
    }

	@Override
	public void onClick(View v) {
		
		
	}

  
}
