
package com.tencent.sample.weiyun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.connect.auth.QQAuth;
import com.tencent.sample.AppConstants;
import com.tencent.sample.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.open.weiyun.RecordManager;

public class RecordOperationActivity extends Activity implements OnClickListener {

    private Button mCreateButton;
    private Button mManageButton;
    private Button mCheckButton;
    private Button mReturnButton;
    private QQAuth mQQAuth;
    private RecordManager mRecordManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weiyun_operation_record_activity);
        initViews();
        mQQAuth = QQAuth.createInstance(AppConstants.APP_ID, this);
        mRecordManager = new RecordManager(this, mQQAuth.getQQToken());
    }

    private void initViews() {
        mCreateButton = (Button) findViewById(R.id.weiyun_operation_record_create);
        mCreateButton.setOnClickListener(this);
        mManageButton = (Button) findViewById(R.id.weiyun_operation_record_manage);
        mManageButton.setOnClickListener(this);
        mCheckButton = (Button) findViewById(R.id.weiyun_operation_record_check);
        mCheckButton.setOnClickListener(this);
        mReturnButton = (Button) findViewById(R.id.weiyun_operation_record_return);
        mReturnButton.setOnClickListener(this);
    }

    private void showAddRecordDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.weiyun_add_record,
                (ViewGroup) findViewById(R.id.dialog));

        new AlertDialog.Builder(this).setTitle("添加记录").setView(layout)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                EditText keyText=(EditText)layout.findViewById(R.id.key);
                                EditText valueText=(EditText)layout.findViewById(R.id.value);
                                String key = keyText.getText().toString();
                                String value=valueText.getText().toString();
                                createRecord(key,value);
                            }
                        })
                .setNegativeButton("取消", null).show();
    }

    private void showCheckRecordDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final EditText keyText=new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入key值").setView(keyText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String key=keyText.getText().toString();
                checkRecord(key);
            }
        }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onClick(View arg0) {
        int viewid = arg0.getId();
        Intent intent = null;
        switch (viewid) {
            case R.id.weiyun_operation_record_create:
                showAddRecordDialog();
                break;
            case R.id.weiyun_operation_record_manage:
                intent = new Intent(this, RecordList.class);
                startActivity(intent);
                break;
            case R.id.weiyun_operation_record_check:
               showCheckRecordDialog();
                break;
            case R.id.weiyun_operation_record_return:
                finish();
                break;

            default:
                break;
        }
    }

    private void createRecord(final String key, final String value){
    	mRecordManager.createRecord(key, value, new IUiListener() {
			@Override
			public void onError(UiError e) {
				Toast.makeText(RecordOperationActivity.this, "写入记录失败:" + e.errorMessage, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(Object response) {
				Toast.makeText(RecordOperationActivity.this, "成功写入一条记录", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onCancel() {
			}
		});
    }

    private void checkRecord(final String key){
    	mRecordManager.checkRecord(key, new IUiListener() {
			@Override
			public void onError(UiError e) {
				Toast.makeText(RecordOperationActivity.this, "查询记录失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(Object response) {
				Boolean result = (Boolean)response;
				if (result) {
					Toast.makeText(RecordOperationActivity.this, "key存在", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(RecordOperationActivity.this, "key不存在", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onCancel() {
			}
		});
    }
}
