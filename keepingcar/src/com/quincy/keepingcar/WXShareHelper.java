package com.quincy.keepingcar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author quincy
 *
 * 1、在需要接收消息的Activity中的create中创建WXShareHelper对象
   2、在需要接收消息的Activity中重写onNewIntent方法：
     @Override
     protected void onNewIntent(Intent intent) {
     super.onNewIntent(intent);

     if (wxShareHelper != null)
     wxShareHelper.onNewIntent(intent);
     }
 * 3、微信不支持同时进行图文分享，只能单独分享一段文字或一张图片。若想同时进行图文分享，可以通过分享一张网页来模拟。
 */
public class WXShareHelper implements IWXAPIEventHandler {
    public static final int SHARE_MONENTS = 1;      // 分享到朋友圈
    public static final int SHARE_FRIEND = 2;      // 分享到朋友

    private IWXAPI mWxApi;
    private Activity mActivity;
    private int mType = SHARE_MONENTS;

    public WXShareHelper(Activity activity){
        mActivity = activity;

        mWxApi = WXAPIFactory.createWXAPI(mActivity, Constants.APP_ID, false);
        if (mWxApi.isWXAppSupportAPI()) {
            mWxApi.registerApp(Constants.APP_ID);
            mWxApi.handleIntent(activity.getIntent(), this);
        }
    }

    public boolean isWXSupportShare(){
        return mWxApi.isWXAppSupportAPI();
    }

    /**
     * @param type 1:分享到朋友圈;2:分享到朋友
     */
    public void setmType(int type) {
        mType = type;
    }

    /**
     * * shareToWeixin分享 * *
     *
     * @param text 这条分享消息被好友点击后的跳转URL
     */
    public void shareText(String text) {
        if (isWXSupportShare()) {
            // 初始化一个WXTextObject对象
            WXTextObject textObj = new WXTextObject();
            textObj.text = text;

            // 用WXTextObject对象初始化一个WXMediaMessage对象
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = textObj;
            msg.description = text;

            // 构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
            req.message = msg;
            req.scene = mType==SHARE_MONENTS? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

            // 调用api接口发送数据到微信
            mWxApi.sendReq(req);
        }
    }

    /**
     * 分享一个图片
     * @author YOLANDA
     * @param imgUrl 要分享的图片
     * @param thumbSize 缩略图size
     */
    public void shareImg(String imgUrl, final int thumbSize){
        if (isWXSupportShare()) {
           // Bitmap shareBitmap = decodeUriAsBitmap(imgUrl);
			shareImage(imgUrl,new ImageToBitmap() {
			
				@Override
				public void setBitmap(Bitmap bitmap) {
					if (bitmap != null) {
						WXImageObject imgObj = new WXImageObject(bitmap);

						WXMediaMessage msg = new WXMediaMessage();
						msg.mediaObject = imgObj;

						Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, thumbSize, thumbSize, true);
						msg.thumbData = bmpToByteArray(thumbBmp);  // 设置缩略图

						SendMessageToWX.Req req = new SendMessageToWX.Req();
						req.transaction = buildTransaction("img");
						req.message = msg;
						req.scene = mType==SHARE_MONENTS? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
						mWxApi.sendReq(req);
					}
				}
			});

            
        }
    }

    /**
     * 分享一个网页
     * @param httpUrl 要分享的连接
     * @param imgUrl 要分享的图片
     * @param title 标题
     * @param description 描述
     */
    public void shareWebPage(final String httpUrl, boolean isToFriend, String imgUrl, final String title, final String description, final int thumbSize){
        if (isWXSupportShare()) {

           // Bitmap icon = decodeUriAsBitmap(imgUrl);
		   shareImage(imgUrl, new ImageToBitmap() {

               @Override
               public void setBitmap(Bitmap bitmap) {
                   if (bitmap != null) {
                       WXWebpageObject webpage = new WXWebpageObject();
                       webpage.webpageUrl = httpUrl;
                       final WXMediaMessage msg = new WXMediaMessage(webpage);
                       msg.description = description;
                       msg.title = title;
                      
                       Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, thumbSize, thumbSize, true);
                       msg.thumbData = bmpToByteArray(thumbBmp);
                       SendMessageToWX.Req req = new SendMessageToWX.Req();
                       req.transaction = buildTransaction("webpage");
                       req.message = msg;
                       req.scene = mType == SHARE_MONENTS ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                       mWxApi.sendReq(req);
                   }
               }
           });
        }
    }

    public void onNewIntent(Intent intent) {
        mActivity.setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
        }
    }

    /**
     * 得到Bitmap的byte
     * @param bmp
     * @param bmp
     * @return
     */
    private static byte[] bmpToByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 构建一个唯一标志
     * @author YOLANDA
     * @param type
     * @return
     */
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : (type + System.currentTimeMillis());
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
		void setBitmap(Bitmap bitmap);
	}
	
}
