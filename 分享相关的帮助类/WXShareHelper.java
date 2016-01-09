package cn.indeo.cloudTV.customer.wxapi;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.indeo.cloudTV.customer.config.Const;

/**
 * Created by caorui on 2015/9/14.
 * <p/>
 * 1、在需要接收消息的Activity中的create中创建WXShareHelper对象
 * 2、在需要接收消息的Activity中重写onNewIntent方法：
 *
 * @Override protected void onNewIntent(Intent intent) {
 * super.onNewIntent(intent);
 * <p/>
 * if (wxShareHelper != null)
 * wxShareHelper.onNewIntent(intent);
 * }
 * 3、微信不支持同时进行图文分享，只能单独分享一段文字或一张图片。若想同时进行图文分享，可以通过分享一张网页来模拟。
 */
public class WXShareHelper implements IWXAPIEventHandler {
    public static final int SHARE_MONENTS = 1;      // 分享到朋友圈
    public static final int SHARE_FRIEND = 2;      // 分享到朋友

    private IWXAPI mWxApi;
    private Activity mActivity;
    private int mType = SHARE_MONENTS;

    public WXShareHelper(Activity activity) {
        mActivity = activity;

        mWxApi = WXAPIFactory.createWXAPI(mActivity, Const.WX_APP_ID, false);
        if (mWxApi.isWXAppSupportAPI()) {
            mWxApi.registerApp(Const.WX_APP_ID);
            mWxApi.handleIntent(activity.getIntent(), this);
        }
    }

    public static boolean isWXSupportShare(Activity activity) {
        IWXAPI mWxApi = WXAPIFactory.createWXAPI(activity, Const.WX_APP_ID, false);
        return mWxApi.isWXAppInstalled();
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
        if (isWXSupportShare(mActivity)) {
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
            req.scene = mType == SHARE_MONENTS ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;

            // 调用api接口发送数据到微信
            mWxApi.sendReq(req);
        }
    }

    /**
     * 分享一个图片
     *
     * @param imgUrl    要分享的图片
     * @param thumbSize 缩略图size
     * @author YOLANDA
     */
    public void shareImg(String imgUrl, final int thumbSize) {
        if (isWXSupportShare(mActivity)) {
            // Bitmap shareBitmap = decodeUriAsBitmap(imgUrl);
            shareImage(imgUrl, new ImageToBitmap() {

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
                        req.scene = mType == SHARE_MONENTS ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        mWxApi.sendReq(req);
                    }
                }
            });


        }
    }

    /**
     * 分享一个网页
     *
     * @param httpUrl     要分享的连接
     * @param imgUrl      要分享的图片
     * @param title       标题
     * @param description 描述
     */
    public void shareWebPage(final String httpUrl, boolean isToFriend, final String imgUrl, final String title, final String description, final int thumbSize) {
        if (isWXSupportShare(mActivity)) {
            // Bitmap icon = decodeUriAsBitmap(imgUrl);
            final Bitmap[] bitmap = {null};
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        bitmap[0] = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                        WXWebpageObject webpage = new WXWebpageObject();
                        webpage.webpageUrl = httpUrl;
                        final WXMediaMessage msg = new WXMediaMessage(webpage);
                        msg.title = title;
                        msg.description = description;
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap[0], thumbSize, thumbSize, true);
                        msg.thumbData = bmpToByteArray(thumbBmp);
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        req.scene = mType == SHARE_MONENTS ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        mWxApi.sendReq(req);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    //分享本地图片
    public void shareWebPageLocal(final String httpUrl, boolean isToFriend, String imgUrl, final String title, final String description, final int thumbSize) {
        if (isWXSupportShare(mActivity)) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = httpUrl;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = description;
//            Bitmap icon= decodeUriAsBitmap(imgUrl);
            Bitmap icon = getScaledBitmap(imgUrl, 50, 50);
            if (icon != null)
                msg.thumbData = bmpToByteArray(icon);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = mType == SHARE_MONENTS ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            mWxApi.sendReq(req);
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
     *
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
     *
     * @param type
     * @return
     * @author YOLANDA
     */
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : (type + System.currentTimeMillis());
    }

    private void shareImage(final String imgUrl, final ImageToBitmap itb) {
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

                } finally {
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

    public interface ImageToBitmap {
        void setBitmap(Bitmap bitmap);
    }

    //本地图片转换成bitmap形式
    public static Bitmap getScaledBitmap(String filePath, int maxWidth, int maxHeight) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap;
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, decodeOptions);
        int actualWidth = decodeOptions.outWidth;
        int actualHeight = decodeOptions.outHeight;
        int desiredWidth = getResizedDimension(maxWidth, maxHeight,
                actualWidth, actualHeight);
        int desiredHeight = getResizedDimension(maxHeight, maxWidth,
                actualHeight, actualWidth);

        decodeOptions.inJustDecodeBounds = false;
        decodeOptions.inSampleSize =
                findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
        Bitmap tempBitmap = BitmapFactory.decodeFile(filePath, decodeOptions);
        if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth ||
                tempBitmap.getHeight() > desiredHeight)) {
            bitmap = Bitmap.createScaledBitmap(tempBitmap,
                    desiredWidth, desiredHeight, true);
            tempBitmap.recycle();
        } else {
            bitmap = tempBitmap;
        }
        return bitmap;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
                                           int actualSecondary) {
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    private static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }
}
