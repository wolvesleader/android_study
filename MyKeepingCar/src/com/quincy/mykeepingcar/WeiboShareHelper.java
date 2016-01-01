package com.quincy.mykeepingcar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 
 *
 * 1、在需要接收消息的Activity（被微博唤起的程序的类）里声明对应的Action： ACTION_SDK_RESP_ACTIVITY
 * <intent-filter>
     <action android:name="com.sina.weibo.sdk.action. ACTION_SDK_RESP_ACTIVITY" />
     <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
    2、在需要接收消息的Activity中onNewIntent方法：@Override
        protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (weiboShareHelper != null)
            weiboShareHelper.onNewIntent(intent);
        }
    3、在需要接收消息的Activity中的create中创建WeiboShareHelper对象
 */
public class WeiboShareHelper{

    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;

    private Activity mActivity;
    private IWeiboShareAPI mWeiboShareAPI = null;
    private int mShareType = SHARE_CLIENT;

    private String mShareText;
    private String mImgUrl;
    private String mTargetUrl;
    private Bitmap mBitmap;

    private WebpageObject mediaObject;
    
    public IWeiboShareAPI getmWeiboShareAPI() {
		return mWeiboShareAPI;
	}

	public WeiboShareHelper(Activity activity, Bundle savedInstanceState){
        mActivity = activity;
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, Constants.APP_KEY);
        mWeiboShareAPI.registerApp();
    }
    
    
    public boolean isWeiboSupportShare() {
        return mWeiboShareAPI.isWeiboAppSupportAPI();
    }
    /**
     * * weibo分享 * *
     *
     * @param text 这条分享消息被好友点击后的跳转URL
     * @param imgUrl 分享的图片URL
     */
    public void shareToWeibo(String text, String imgUrl,String targetUrl) {
        mShareText = text;
        mImgUrl = imgUrl;
        mTargetUrl = targetUrl;
        final boolean bShareText = (text != null);
        final  boolean bShareImg = (imgUrl != null);
        final  boolean bTargetUrl = (targetUrl != null);
       
        shareImage(imgUrl,new ImageToBitmap() {
			
			@Override
			public void setBitmap(Bitmap bitmap) {
				mBitmap = bitmap;
				sendMessage(bShareText, bShareImg, bTargetUrl, false, false, false);
			}
		});
    }

    public void onNewIntent(Intent intent) {
     
        mWeiboShareAPI.handleWeiboResponse(intent, (IWeiboHandler.Response)mActivity);
    }
    
    

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
     */
    private void sendMessage(boolean hasText, boolean hasImage,
                             boolean hasWebpage, boolean hasMusic, boolean hasVideo, boolean hasVoice) {

        if (mShareType == SHARE_CLIENT) {
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    sendMultiMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
                } else {
                    sendSingleMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo/*, hasVoice*/);
                }
            } else {
                //Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT).show();
            }
        }
        else if (mShareType == SHARE_ALL_IN_ONE) {
            sendMultiMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                                  boolean hasMusic, boolean hasVideo, boolean hasVoice) {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }

        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        if (mShareType == SHARE_CLIENT) {
           mWeiboShareAPI.sendRequest(request);
        	//mWeiboShareAPI.sendRequest(mActivity,request);
        }
        else if (mShareType == SHARE_ALL_IN_ONE) {
            /*AuthInfo authInfo = new AuthInfo(mActivity, Const.WB_APP_KEY, Const.WB_REDIRECT_URL, Const.WB_SCOPE);
            Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mActivity.getApplicationContext());
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
            mWeiboShareAPI.sendRequest(mActivity, request, authInfo, token, new WeiboAuthListener() {

                @Override
                public void onWeiboException( WeiboException arg0 ) {
                }

                @Override
                public void onComplete( Bundle bundle ) {
                    // TODO Auto-generated method stub
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(mActivity.getApplicationContext(), newToken);
                    //Toast.makeText(getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), 0).show();
                }

                @Override
                public void onCancel() {
                }
            });*/
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     *
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     */
    private void sendSingleMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                                   boolean hasMusic, boolean hasVideo/*, boolean hasVoice*/) {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            weiboMessage.mediaObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.mediaObject = getImageObj();
        }
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/

        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
       mWeiboShareAPI.sendRequest(request);
        //mWeiboShareAPI.sendRequest(mActivity,request);
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = mShareText;
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
    	 ImageObject imageObject = new ImageObject();
	      imageObject.setImageObject(mBitmap);
	      return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
    	WebpageObject webObj = new WebpageObject();
    	webObj.identify = Utility.generateGUID();
    	webObj.title = "分享";
    	webObj.description = mShareText;

        // 设置 Bitmap 类型的图片到视频对象里
        Bitmap  bmp = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher);
        webObj.setThumbImage(bmp);
        webObj.actionUrl =mTargetUrl;
        webObj.defaultText = mShareText;
        return webObj;
          
    }

    /**
     * 创建多媒体（音乐）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj() {
        return null;
    }

    /**
     * 创建多媒体（视频）消息对象。
     *
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj() {
        return null;
    }

    /**
     * 创建多媒体（音频）消息对象。
     *
     * @return 多媒体（音乐）消息对象。
     */
    private VoiceObject getVoiceObj() {
        return null;
    }
    private void shareImage(final String imgUrl,final ImageToBitmap itb){
        new Thread() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                InputStream in = null;
                try {

                    URL url = new URL(imgUrl);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");// 请求方法
                    conn.setConnectTimeout(5000);// 连接超时
                    conn.setReadTimeout(5000);// 读取超时, 连接上了,但服务器迟迟不给反馈

                    conn.connect();

                    int responseCode = conn.getResponseCode();// 响应码
                    if (responseCode == 200) {
                        in = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        itb.setBitmap(bitmap);
                    }

                } catch (Exception e) {

                } finally{
                    if (conn != null) {
                        conn.disconnect();// 断开连接
                    }
                    try {
                        in.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public interface ImageToBitmap{
        void setBitmap(Bitmap bitmap);
    }
}
