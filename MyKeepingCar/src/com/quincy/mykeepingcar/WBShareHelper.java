/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;


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
 
public class WBShareHelper implements IWeiboHandler.Response {
    @SuppressWarnings("unused")
    private static final String TAG = "WBShareActivity";

    public static final String KEY_SHARE_TYPE = "key_share_type";
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    private Activity mActivity;
    
   
    
    /** 微博微博分享接口实例 */
    private IWeiboShareAPI  mWeiboShareAPI = null;

    private int mShareType = SHARE_CLIENT;
    
    
    public WBShareHelper(Activity activity,Bundle savedInstanceState){
    	this.mActivity = activity;
    	 mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, Constants.APP_KEY);
		 mWeiboShareAPI.registerApp();
		 if (savedInstanceState != null) {
	            mWeiboShareAPI.handleWeiboResponse(activity.getIntent(), this);
	     }
    }
    
    /**
     * 判断是否安装了微博客户端
     * @return
     */
    public boolean isWeiboSupportShare() {
        return mWeiboShareAPI.isWeiboAppSupportAPI();
    }
    
    
  
    protected void onNewIntent(Intent intent) {
    	mActivity.setIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }
    
   
    
    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
     */
    public void sendMessage(TextObject textObj, ImageObject imgObj,
    		WebpageObject webObj, MusicObject musicObj, 
    		VideoObject videoObj, VoiceObject voiceObject) {

        if (mShareType == SHARE_CLIENT) {
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351) {
                    sendMultiMessage(textObj, imgObj, webObj, musicObj, videoObj, voiceObject);
                } else {
                    sendSingleMessage(textObj, imgObj, webObj, musicObj, videoObj/*, hasVoice*/);
                }
            } else {
                Toast.makeText(mActivity, "没有安装微博客户端", Toast.LENGTH_SHORT).show();
            }
        }else if (mShareType == SHARE_ALL_IN_ONE) {
            sendMultiMessage(textObj, imgObj, webObj, musicObj, videoObj, voiceObject);
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
    private void sendMultiMessage(TextObject textObj, ImageObject imgObj,
    		WebpageObject webObj, MusicObject musicObj, 
    		VideoObject videoObj, VoiceObject voiceObject) {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        
        if (textObj  != null) {
            weiboMessage.textObject = textObj;
        }

        if (imgObj != null) {
            weiboMessage.imageObject = imgObj;
        }

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (webObj != null) {
            weiboMessage.mediaObject = webObj;
        }
        if (musicObj != null) {
            weiboMessage.mediaObject = musicObj;
        }
        if (videoObj != null) {
            weiboMessage.mediaObject = videoObj;
        }
       
        if (voiceObject != null) {
            weiboMessage.mediaObject = voiceObject;
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
    private void sendSingleMessage(TextObject textObj, ImageObject imgObj,
    		WebpageObject webObj, MusicObject musicObj, 
    		VideoObject videoObj/*, boolean hasVoice*/) {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (textObj != null) {
            weiboMessage.mediaObject = textObj;
        }
        if (imgObj != null) {
            weiboMessage.mediaObject = imgObj;
        }
        if (webObj != null) {
            weiboMessage.mediaObject = webObj;
        }
        if (musicObj != null) {
            weiboMessage.mediaObject = musicObj;
        }
        if (videoObj  != null) {
            weiboMessage.mediaObject = videoObj;
        }

        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
        //mWeiboShareAPI.sendRequest(mActivity,request);
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
                    conn.setConnectTimeout(15000);// 连接超时
                    conn.setReadTimeout(15000);// 读取超时, 连接上了,但服务器迟迟不给反馈

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
        ImageObject setBitmap(Bitmap bitmap);
    }
    

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     * 
     * @param baseRequest 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
        case WBConstants.ErrorCode.ERR_OK:
        	Toast.makeText(mActivity, "分享成功！", Toast.LENGTH_LONG).show();
            break;
        case WBConstants.ErrorCode.ERR_CANCEL:
        	Toast.makeText(mActivity, "分享取消！", Toast.LENGTH_LONG).show();
            break;
        case WBConstants.ErrorCode.ERR_FAIL:
        	Toast.makeText(mActivity, "分享拒绝！", Toast.LENGTH_LONG).show();
            break;
        }
    }
    
   

}
