package com.quincy.mykeepingcar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 测试新浪微博登录模块的demo,和分享的模块
 * 
 * @author quincy
 * 
 */
public class MainActivity extends FragmentActivity implements IWeiboHandler.Response {

	public static final int SHARE_CLIENT = 1;
	public static final int SHARE_ALL_IN_ONE = 2;
	private WBShareHelper shareHelper;
	private IWeiboShareAPI mWeiboShareAPI = null;

	private WeiboShareHelper weiboShareHelper;
	 private Bundle mBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//weiboShareHelper = new WeiboShareHelper(this, savedInstanceState);
		mBundle = savedInstanceState;
	}


	@Override
    protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    if (weiboShareHelper != null)
	        weiboShareHelper.onNewIntent(intent);
    }
	
	  /**
		 * 接收微客户端博请求的数据。 当微博客户端唤起当前应用并进行分享时，该方法被调用。
		 * 
		 * @param baseResp
		 *            微博请求数据对象
		 * @see {@link IWeiboShareAPI#handleWeiboRequest}
		 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
			break;
		}
	}
		
	
	public void shareShaCun(View v) {
		
		if (weiboShareHelper == null)
			weiboShareHelper = new WeiboShareHelper(MainActivity.this, mBundle);
		
		//ArrayList<String> list = new ArrayList<String>();
		//list.add("http://pic.autobobo.com//repair//425204a0-9008-4ef3-a85c-c0ddaa74485f.jpg");
		// 在这里先把图片请求回来
		weiboShareHelper.shareToWeibo("傻聪分享",
				"http://pic.autobobo.com//repair//425204a0-9008-4ef3-a85c-c0ddaa74485f.jpg",
		"http://www.autobobo.com");

	}

	/**
	 * 分享文本
	 * 
	 * @param v
	 */
	public void share(View v) {
		TextObject textObj = new TextObject();
		textObj.text = "测试数据";
		shareHelper.sendMessage(textObj, null, null, null, null, null);
	}

	public void shareWeb(View v) {
		ImageObject imageObject = new ImageObject();
		// 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
		Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		imageObject.setImageObject(bitmap1);

		TextObject textObj = new TextObject();
		textObj.text = "测试数据";
		shareHelper.sendMessage(textObj, null, null, null, null, null);

		WebpageObject webObj = new WebpageObject();
		webObj.title = "烩车网";
		webObj.identify = Utility.generateGUID();
		webObj.description = "汽车后市场的一个平台";
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		// 设置 Bitmap 类型的图片到视频对象里 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
		webObj.setThumbImage(bitmap);

		webObj.actionUrl = "http://www.autobobo.com";
		webObj.defaultText = "Webpage默认文案";
		shareHelper.sendMessage(textObj, imageObject, webObj, null, null, null);
	}

	public boolean isWeiboSupportShare() {
		return mWeiboShareAPI.isWeiboAppSupportAPI();
	}
	
}
