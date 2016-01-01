package com.quincy.myshareqq;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ分享
 * 
 * @author quincy
 * 
 */
public class MainActivity extends Activity {

	public static Tencent mTencent;
	private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTencent = Tencent.createInstance("222222", this);
	}

	public void onClickShare(View v) {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,
				"http://www.qq.com/news/1.html");
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
				"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
		// params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
		mTencent.shareToQQ(MainActivity.this, params, qqShareListener);
	}

	public void shareToQzone (View v) {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
		//params.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );
	    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
	    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
	    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.autobobo.com");//必填
	    ArrayList<String> imageUrls = new ArrayList<String>();
	   // http://img.pinkan.com//topic//20151214//145008037334649.jpg
	   // imageUrls.add("http://img.pinkan.com//topic//20151214//145008037334649.jpg");
	   // imageUrls.add("http://img.pinkan.com//topic//20151214//145008037334649.jpg");
	   // imageUrls.add("http://img.pinkan.com//topic//20151214//145008037334649.jpg");
	    //http://pic.autobobo.com/
	    imageUrls.add("http://pic.autobobo.com//default.png");
	    //imageUrls.add("http://pic.autobobo.com");
	    //imageUrls.add("http://img.pinkan.com/topic/20151214/145008037334649.jpg");
	    //params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, "http://pic.autobobo.com//repair//425204a0-9008-4ef3-a85c-c0ddaa74485f.jpg");
	    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
	   // params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl);
	    mTencent.shareToQzone(MainActivity.this, params, qqShareListener);
	}
	
	
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    	Tencent.onActivityResultData(requestCode,resultCode,data,qqShareListener);
	        if (requestCode == Constants.REQUEST_QQ_SHARE) {
	        	if (resultCode == Constants.ACTIVITY_OK) {
	        		Tencent.handleResultData(data, qqShareListener);
	        	}
	        } /*else {
	        	String path = null;
	            if (resultCode == Activity.RESULT_OK && requestCode == 0) {
	                if (data != null && data.getData() != null) {
	                    // 根据返回的URI获取对应的SQLite信息
	                    Uri uri = data.getData();
	                    path = Util.getPath(this, uri);
	                }
	            }
	            if (path != null) {
	                imageUrl.setText(path);
	            } else {
	            	if(shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE){
	            		showToast("请重新选择图片");
	            	}
	            }
	        }*/
	    }

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onCancel() {
			if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
				Util.toastMessage(MainActivity.this, "onCancel: ");
			}
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			Util.toastMessage(MainActivity.this,
					"onComplete: " + response.toString());
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			Util.toastMessage(MainActivity.this, "onError: " + e.errorMessage,
					"e");
		}
	};

}
