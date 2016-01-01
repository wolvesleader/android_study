
package com.tencent.sample.activitys;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.sample.R;
import com.tencent.sample.ThreadManager;
import com.tencent.sample.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QZoneShareActivity extends BaseActivity implements OnClickListener {

	private static final int MAX_IMAGE = 9;
    private RadioButton mRadioBtnShareTypeImgAndText;
    private RadioButton mRadioBtnShareTypeImg;
    private RadioButton mRadioBtnShareTypeApp;

    private EditText title = null;
    private EditText summary = null;
    private EditText targetUrl = null;
    //QZone分享， SHARE_TO_QQ_TYPE_DEFAULT 图文，SHARE_TO_QQ_TYPE_IMAGE 纯图
    private int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
    // zivon add
    private LinearLayout mImageContainerLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarTitle("Qzone分享");
        setLeftButtonEnable();
        setContentView(R.layout.qzone_share_activity);

        title = (EditText) findViewById(R.id.shareqq_title);
        targetUrl = (EditText) findViewById(R.id.shareqq_targetUrl);
        summary = (EditText) findViewById(R.id.shareqq_summary);
        mImageContainerLayout = (LinearLayout) findViewById(R.id.images_picker_layout);

        findViewById(R.id.shareqq_commit).setOnClickListener(this);
        findViewById(R.id.btn_addImage).setOnClickListener(this);
        mRadioBtnShareTypeImgAndText = (RadioButton) findViewById(R.id.QZoneShare_radioBtn_image_text_share);
        mRadioBtnShareTypeImgAndText.setOnClickListener(this);
        mRadioBtnShareTypeImg = (RadioButton)findViewById(R.id.QZoneShare_radioBtn_image_share);
        mRadioBtnShareTypeImg.setOnClickListener(this);

        mRadioBtnShareTypeApp = (RadioButton)findViewById(R.id.QZoneShare_radioBtn_app_share);
        mRadioBtnShareTypeApp.setOnClickListener(this);

        checkTencentInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.QZoneShare_radioBtn_image_text_share:
                shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
                targetUrl.setEnabled(true);
                break;
            case R.id.QZoneShare_radioBtn_image_share:
                shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE;
                targetUrl.setEnabled(true);
                break;
            case R.id.QZoneShare_radioBtn_app_share:
                shareType = QzoneShare.SHARE_TO_QZONE_TYPE_APP;
                targetUrl.setEnabled(false);
                break;
            case R.id.shareqq_commit: // 提交
                final Bundle params = new Bundle();
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title.getText().toString());
                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary.getText().toString());
                if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
                    //app分享不支持传目标链接
                    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl.getText().toString());
                }
                // 支持传多个imageUrl
                ArrayList<String> imageUrls = new ArrayList<String>();
                for (int i = 0; i < mImageContainerLayout.getChildCount(); i++) {
                    LinearLayout addItem = (LinearLayout) mImageContainerLayout.getChildAt(i);
                    EditText editText = (EditText) addItem.getChildAt(1);
                    imageUrls.add(editText.getText().toString());
                }
                // String imageUrl = "XXX";
                // params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl);
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
                doShareToQzone(params);
                return;
            case R.id.btn_addImage:
                int num = mImageContainerLayout.getChildCount();
                if (num < MAX_IMAGE) {
                    LinearLayout addItem = (LinearLayout) LayoutInflater.from(this).inflate(
                            R.layout.image_picker_layout, null);
                    mImageContainerLayout.addView(addItem);
                    TextView textView0 = (TextView) addItem.getChildAt(0); // index
                    View view1 = addItem.getChildAt(1); // editText url
                    View view2 = addItem.getChildAt(2); // picker按钮
                    View view3 = addItem.getChildAt(3); // 删除按钮
                    textView0.setText(String.valueOf(num + 1));
                    view1.setId(1000 + num); // url EditText
                    view2.setId(2000 + num); // picker
                    view3.setId(3000 + num); // 删除
                    addItem.setId(num);
                    view2.setOnClickListener(this);
                    view3.setOnClickListener(this);
                } else {
                    showToast("不能添加更多的图片!!!");
                }
                return;
        }
        if (id >= 2000 && id < 3000) {
            // 点的是选择图片
            startPickLocaleImage(this, id - 2000);
        } else if (id >= 3000 && id < 4000) {
            // 点的是删除图片
            if (mImageContainerLayout.getChildCount() > 0) {
                View view = mImageContainerLayout.findViewById(id - 3000);
                mImageContainerLayout.removeView(view);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MainActivity.mTencent != null) {
            MainActivity.mTencent.releaseResource();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Tencent.onActivityResultData(requestCode,resultCode,data,qZoneShareListener);
        if (requestCode == Constants.REQUEST_QZONE_SHARE) {
        	if (resultCode == Constants.ACTIVITY_OK) {
        		Tencent.handleResultData(data, qZoneShareListener);
        	}
        } else {
        	String path = null;
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    // 根据返回的URI获取对应的SQLite信息
                    Uri uri = data.getData();
                    path = Util.getPath(this, uri);
                }
            }
            if (path != null) {
            	// 这里很奇葩的方式, 将获取到的值赋值给相应的EditText, 竟然能对应上
            	EditText editText = (EditText)mImageContainerLayout.findViewById(requestCode + 1000);
            	editText.setText(path);
            } else {
                showToast("请重新选择图片");
            }
        }
    }

    private static final void startPickLocaleImage(Activity activity, int requestId) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (android.os.Build.VERSION.SDK_INT >= Util.Build_VERSION_KITKAT) {
            intent.setAction(Util.ACTION_OPEN_DOCUMENT);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(
                Intent.createChooser(intent, activity.getString(R.string.str_image_local)), requestId);
    }
    
    IUiListener qZoneShareListener = new IUiListener() {

        @Override
        public void onCancel() {
            Util.toastMessage(QZoneShareActivity.this, "onCancel: ");
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Util.toastMessage(QZoneShareActivity.this, "onError: " + e.errorMessage, "e");
        }

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			 Util.toastMessage(QZoneShareActivity.this, "onComplete: " + response.toString());
		}

    };
    
    /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
        // QZone分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                if (null != MainActivity.mTencent) {
                    MainActivity.mTencent.shareToQzone(QZoneShareActivity.this, params, qZoneShareListener);
                }
            }
        });
    }

    Toast mToast = null;
    private void showToast(String text) {
        if (mToast != null && !super.isFinishing()) {
            mToast.setText(text);
            mToast.show();
            return;
        }
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
