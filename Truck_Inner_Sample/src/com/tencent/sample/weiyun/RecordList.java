package com.tencent.sample.weiyun;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.sample.AppConstants;
import com.tencent.sample.R;
import com.tencent.sample.Util;
import com.tencent.open.weiyun.RecordManager;

/**
 * Created with IntelliJ IDEA.
 * User: melody
 * Date: 13-4-27
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public class RecordList extends Activity {
    private ListView mListView;
    private List<RecordListAdapter.RecordItem> list;
    private RecordListAdapter mAdapter;
    private QQAuth mQQAuth;
    private RecordManager mRecordManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weiyun_filelist_activity);
        mListView = (ListView) findViewById(R.id.filelist_listview);
        mAdapter=new RecordListAdapter(this,itemClick);
        mQQAuth=QQAuth.createInstance(AppConstants.APP_ID,this);
        mRecordManager = new RecordManager(this, mQQAuth.getQQToken());
        mListView.setAdapter(mAdapter);
        getList();
    }

    private void getList(){
    	mRecordManager.queryAllRecord(new IUiListener() {
			@Override
			public void onError(UiError e) {
				Toast.makeText(RecordList.this, "获取记录列表失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(Object response) {
				List<String> keyList = (List<String>)response;
				list=new ArrayList<RecordListAdapter.RecordItem>();
				for (int i = 0; i < keyList.size(); i++) {
				    RecordListAdapter.RecordItem item = new RecordListAdapter.RecordItem();
				    item.key = keyList.get(i);
				    item.value = "";
				    list.add(item);
				}  
                mAdapter.record_list=list;
                mAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onCancel() {
				
			}
		});
    }

    private void deleteRecord(final int position){
    	RecordListAdapter.RecordItem item=list.get(position);
    	mRecordManager.deleteRecord(item.key, new IUiListener() {
			@Override
			public void onError(UiError e) {
				Toast.makeText(RecordList.this, "删除记录失败", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onComplete(Object response) {
				list.remove(position);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(RecordList.this, "删除记录成功", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCancel() {
				
			}
		});
    }

    private void modifyRecord(final int position, final String value){
    	RecordListAdapter.RecordItem item=list.get(position);
    	mRecordManager.modifyRecord(item.key, value, new IUiListener() {
			@Override
			public void onError(UiError e) {
				Toast.makeText(RecordList.this, "修改记录失败", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onComplete(Object response) {
				Toast.makeText(RecordList.this, "成功修改记录", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCancel() {
			}
		});
    }

    private void getRecord(final int position){
    	final RecordListAdapter.RecordItem item=list.get(position);
    	mRecordManager.getRecord(item.key, new IUiListener() {
			@Override
			public void onError(UiError e) {
				Toast.makeText(RecordList.this, "查询记录失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(Object response) {
				Toast.makeText(RecordList.this, "记录"+Util.hexToString(item.key)+"的值是："+ (String)response, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onCancel() {
			}
		});
    }

    private IRecordListAdapterItemClick itemClick=new IRecordListAdapterItemClick() {
        @Override
        public void onDeleteClick(int position) {
            deleteRecord(position);
        }

        @Override
        public void onModifyClick(final int position) {
            final EditText valueText=new EditText(RecordList.this);
            new AlertDialog.Builder(RecordList.this).setTitle("请输入值").setView(valueText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String value=valueText.getText().toString();
                    modifyRecord(position,value);
                }
            }).setNegativeButton("取消", null).show();
        }

        @Override
        public void onGetClick(int position) {
            getRecord(position);
        }
    };
}
