package cn.indeo.cloudTV.customer.widget.WeiBo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.text.TextUtils;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.indeo.cloudTV.customer.R;
import cn.indeo.cloudTV.customer.config.Const;

/*import cn.indeo.cloudTV.customer.config.Const;
import cn.indeo.cloudTV.customer.ui.Activity.TopicDetailActivity;
import customer.app_base.ToastUtil;*/

/**
 * Created by caorui on 2015/9/14.
 * <p/>
 * 1、在需要接收消息的Activity（被微博唤起的程序的类）里声明对应的Action： ACTION_SDK_RESP_ACTIVITY
 * <intent-filter>
 * <action android:name="com.sina.weibo.sdk.action. ACTION_SDK_RESP_ACTIVITY" />
 * <category android:name="android.intent.category.DEFAULT" />
 * </intent-filter>
 * 2、在需要接收消息的Activity中onNewIntent方法：@Override
 * protected void onNewIntent(Intent intent) {
 * super.onNewIntent(intent);
 * <p/>
 * if (weiboShareHelper != null)
 * weiboShareHelper.onNewIntent(intent);
 * }
 * 3、在需要接收消息的Activity中的create中创建WeiboShareHelper对象
 */
public class WeiboShareHelper {

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
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private Bitmap bitmap2;

    public WeiboShareHelper(Activity activity) {
        mActivity = activity;
    }


    public static boolean isWeiboSupportShare(Activity activity) {
        IWeiboShareAPI weiboAPI = WeiboShareSDK.createWeiboAPI(activity, Const.WB_APP_KEY);
        return weiboAPI.isWeiboAppInstalled();
    }

    /**
     * * weibo分享 * *
     *
     * @param text   这条分享消息被好友点击后的跳转URL
     * @param imgUrl 分享的图片URL
     */
    public void shareToWeibo(final String imgUrl, String targetUrl, String text) {
        if (mWeiboShareAPI == null) {
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, Const.WB_APP_KEY);
        }
        mWeiboShareAPI.registerApp();

        mShareText = text;
        mImgUrl = imgUrl;
        mTargetUrl = targetUrl;
        final boolean bShareText = !TextUtils.isEmpty(text);
        final boolean bShareImg = !TextUtils.isEmpty(imgUrl);
        final boolean bTargetUrl = (targetUrl != null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap2 = BitmapFactory.decodeStream(new URL(imgUrl).openStream());

                    sendMessage(bShareImg, bTargetUrl, bShareText, false, false, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onNewIntent(Intent intent) {
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, (IWeiboHandler.Response) this);
    }

    public IWeiboShareAPI getmWeiboShareAPI() {
        return mWeiboShareAPI;
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     *
     * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
     */
    private void sendMessage(boolean hasImage,
                             boolean hasWebpage, boolean hasText, boolean hasMusic, boolean hasVideo, boolean hasVoice) {

        if (mShareType == SHARE_CLIENT) {
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    sendMultiMessage(hasImage, hasWebpage, hasText, hasMusic, hasVideo, hasVoice);
                } else {
                    sendSingleMessage(hasImage, hasWebpage, hasText, hasMusic, hasVideo/*, hasVoice*/);
                }
            } else {
                //Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT).show();
            }
        } else if (mShareType == SHARE_ALL_IN_ONE) {
            sendMultiMessage(hasImage, hasWebpage, hasText, hasMusic, hasVideo, hasVoice);
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
    private void sendMultiMessage(boolean hasImage, boolean hasWebpage, boolean hasText,
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
        } else if (mShareType == SHARE_ALL_IN_ONE) {

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
    private void sendSingleMessage(boolean hasImage, boolean hasWebpage, boolean hasText,
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
        /*ImageObject imageObject = new ImageObject();
           //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap  bitmap1 = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher);
        imageObject.setImageObject(bitmap1);
        return imageObject;*/
        ImageObject imageObject = new ImageObject();
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth(), bitmap2.getHeight(), true);
        imageObject.setImageObject(thumbBmp);
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
        webObj.title = "";
        if (!TextUtils.isEmpty(mShareText)) {
            webObj.description = mShareText;
        }else {
            webObj.description = "";
        }
        // 设置 Bitmap 类型的图片到视频对象里 drawable随便写
        Bitmap bmp = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.logo);
        webObj.setThumbImage(bmp);
        webObj.actionUrl = mTargetUrl;
        if (!TextUtils.isEmpty(mShareText)) {
            webObj.defaultText = mShareText;
        }else {
            webObj.defaultText = "";
        }
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

    // 分享本地图片
    public void shareToWeiboNative(String text, String imgUrl, String targetUrl) {
        if (mWeiboShareAPI == null) {
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, Const.WB_APP_KEY);
        }
        mWeiboShareAPI.registerApp();
        mShareText = text;
        String mImgUrl = imgUrl;
        mTargetUrl = targetUrl;
//        bitmap = getScaledBitmap(imgUrl,50,50);
        if (!TextUtils.isEmpty(imgUrl)) {
            bitmap = getImage(imgUrl);
        }
        final boolean bShareText = (text != null);
        final boolean bShareImg = (imgUrl != null);
        final boolean bTargetUrl = (targetUrl != null);
        sendMessageNative(bShareText, bShareImg, bTargetUrl, false, false, false);
    }

    private void sendMessageNative(boolean hasText, boolean hasImage,
                                   boolean hasWebpage, boolean hasMusic, boolean hasVideo, boolean hasVoice) {

        if (mShareType == SHARE_CLIENT) {
            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
                if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                    sendMultiMessageNative(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
                } else {
                    sendSingleMessageNative(hasText, hasImage, hasWebpage, hasMusic, hasVideo/*, hasVoice*/);
                }
            } else {
                //Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT).show();
            }
        } else if (mShareType == SHARE_ALL_IN_ONE) {
            sendMultiMessageNative(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
        }
    }

    private void sendMultiMessageNative(boolean hasText, boolean hasImage, boolean hasWebpage,
                                        boolean hasMusic, boolean hasVideo, boolean hasVoice) {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }

        if (hasImage) {
            weiboMessage.imageObject = getImageObjNative();
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
        } else if (mShareType == SHARE_ALL_IN_ONE) {

        }
    }

    private void sendSingleMessageNative(boolean hasText, boolean hasImage, boolean hasWebpage,
                                         boolean hasMusic, boolean hasVideo/*, boolean hasVoice*/) {

        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            weiboMessage.mediaObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.mediaObject = getImageObjNative();
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
    }

    private ImageObject getImageObjNative() {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(bitmap);
        return imageObject;
    }


    private static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 压缩图片大小
     *
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
