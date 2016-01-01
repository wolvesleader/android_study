
package com.tencent.sample.weiyun;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;
import android.widget.Toast;

import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.tencent.sample.AppConstants;
import com.tencent.sample.R;
import com.tencent.open.weiyun.FileManager;
import com.tencent.open.weiyun.FileManager.WeiyunFileType;
import com.tencent.open.weiyun.IDownLoadFileCallBack;
import com.tencent.open.weiyun.IGetFileListListener;
import com.tencent.open.weiyun.WeiyunFile;

public class FileListActivity extends Activity {
	private static final String SAVE_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private ListView mListView;
    private FileListAdapter mAdapter;

    private int current_actiontype; // 当前操作类型：图片、音频、视频
    WeiyunFileType mFileType;
    private List<WeiyunFile> mFileList;
    private QQAuth mQQAuth;
    private FileManager mFileManager;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weiyun_filelist_activity);

        mProgress = new ProgressDialog(this);

        mListView = (ListView) findViewById(R.id.filelist_listview);
        mAdapter = new FileListAdapter(this, itemClick);
        mListView.setAdapter(mAdapter);

        Intent intent = getIntent();
        mFileType = WeiyunFileType.ImageFile;
        current_actiontype = intent.getExtras().getInt("actiontype");
        switch (current_actiontype) {
            case WeiyunMainActivity.ACTION_PICTURE:
                mFileType = WeiyunFileType.ImageFile;
                break;
            case WeiyunMainActivity.ACTION_MUSIC:
                mFileType = WeiyunFileType.MusicFile;
                break;
            case WeiyunMainActivity.ACTION_VIDEO:
                mFileType = WeiyunFileType.VideoFile;
                break;
            default:
                break;
        }

        mFileList = new ArrayList<WeiyunFile>();
        mQQAuth = QQAuth.createInstance(AppConstants.APP_ID, this);
        mFileManager = new FileManager(this, mQQAuth.getQQToken());
        mFileManager.getFileList(mFileType,  new IGetFileListListener() {
			
			@Override
			public void onError(UiError e) {
				if (FileListActivity.this.isFinishing()) {
					return;
				}
				mProgress.dismiss();
				Toast.makeText(getApplicationContext(), "查询文件列表失败 ：" + e.errorMessage, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onComplete(List<WeiyunFile> fileList) {
				if (FileListActivity.this.isFinishing()) {
					return;
				}
				mProgress.dismiss();
				mFileList = fileList;
				mAdapter.setData(mFileList, current_actiontype);
				mAdapter.notifyDataSetChanged();
				Toast.makeText(FileListActivity.this, "查询列表成功", Toast.LENGTH_SHORT).show();
			}
		});
        
        mProgress.setMessage("正在查询文件列表，请稍候...");
        mProgress.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        //file_info_list.clear();
        mProgress.dismiss();
    }

    private IFileListAdapterItemClick itemClick = new IFileListAdapterItemClick() {

        @Override
        public void onThumbPicClick(int position) {
        	final WeiyunFile file = mFileList.get(position);
        	mFileManager.downLoadThumb(file, SAVE_FILE_PATH + file.getFileName(), "128*128", new IDownLoadFileCallBack() {
				@Override
				public void onPrepareStart() {
					if (isFinishing()) {
						return;
					}
					mProgress.setMessage("正在获取缩略图，请稍等.....");
					mProgress.show();
				}
				
				@Override
				public void onError(UiError e) {
					if (isFinishing()) {
						return;
					}
					mProgress.dismiss();
					if (e.errorCode == Constants.ERROR_FILE_EXISTED) {
						String filePath = "/sdcard/" + file.getFileName();
						Toast.makeText(FileListActivity.this,
								"文件下载成功，路径是:" + filePath + "",
								Toast.LENGTH_SHORT).show();
						if (mFileType == WeiyunFileType.ImageFile) {
							ImageViewDialog dlg = new ImageViewDialog(
									FileListActivity.this, filePath);
							dlg.show();
						}
					} else {
						Toast.makeText(FileListActivity.this, "文件下载失败:" + e.errorCode + "," + e.errorMessage,
								Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onDownloadSuccess(String filepath) {
					if (isFinishing()) {
						return;
					}
					mProgress.dismiss();
					Toast.makeText(FileListActivity.this,  "获取缩略图成功，路径是:" + filepath + "",
							Toast.LENGTH_SHORT).show();
					if (mFileType == WeiyunFileType.ImageFile) {
						ImageViewDialog dlg = new ImageViewDialog(
								FileListActivity.this, filepath);
						dlg.show();
					}
				}
				
				@Override
				public void onDownloadStart() {
					mProgress.setMessage("文件正在下载，请稍等.....");
				}
				
				@Override
				public void onDownloadProgress(int progress) {
					mProgress.setMessage("文件正在下载: "+progress+"%   "+"请稍等.....");
				}
			});        	
        }

        @Override
        public void onDownloadClick(int position, final int actiontype) {
        	final WeiyunFile file = mFileList.get(position);
        	mFileManager.downLoadFile(mFileType, file, SAVE_FILE_PATH + file.getFileName(), new IDownLoadFileCallBack() {
				@Override
				public void onPrepareStart() {
					if (isFinishing()) {
						return;
					}
					mProgress.setMessage("文件下载准备中，请稍等.....");
					mProgress.show();
				}
				
				@Override
				public void onError(UiError e) {
					if (isFinishing()) {
						return;
					}
					mProgress.dismiss();
					if (e.errorCode == Constants.ERROR_FILE_EXISTED) {
						String filePath = "/sdcard/" + file.getFileName();
						Toast.makeText(FileListActivity.this,
								"文件下载成功，路径是:" + filePath + "",
								Toast.LENGTH_SHORT).show();
						if (mFileType == WeiyunFileType.ImageFile) {
							ImageViewDialog dlg = new ImageViewDialog(
									FileListActivity.this, filePath);
							dlg.show();
						}
					} else {
						Toast.makeText(FileListActivity.this, "文件下载失败:" + e.errorCode + "," + e.errorMessage,
								Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onDownloadSuccess(String filepath) {
					if (isFinishing()) {
						return;
					}
					mProgress.dismiss();
					Toast.makeText(FileListActivity.this,
							"文件下载成功，路径是:" + filepath + "",
							Toast.LENGTH_SHORT).show();
					if (mFileType == WeiyunFileType.ImageFile) {
						ImageViewDialog dlg = new ImageViewDialog(
								FileListActivity.this, filepath);
						dlg.show();
					}
				}
				
				@Override
				public void onDownloadStart() {
					mProgress.setMessage("文件正在下载，请稍等.....");
				}
				
				@Override
				public void onDownloadProgress(int progress) {
					mProgress.setMessage("文件正在下载: "+progress+"%   "+"请稍等.....");
				}
			});
        }

        @Override
        public void onDeleteClick(final int position, int actiontype) {
            new Thread() {
                public void run() {
                    final WeiyunFile item=mFileList.get(position);
                    WeiyunFileType fileType = WeiyunFileType.ImageFile;
                    switch (current_actiontype) {
                        case WeiyunMainActivity.ACTION_PICTURE:
                            fileType = WeiyunFileType.ImageFile;
                            break;
                        case WeiyunMainActivity.ACTION_MUSIC:
                            fileType = WeiyunFileType.MusicFile;
                            break;
                        case WeiyunMainActivity.ACTION_VIDEO:
                            fileType = WeiyunFileType.VideoFile;
                            break;
                        default:
                            break;
                    }
                    mFileManager.deleteFile(fileType, item.getFileId(), new IUiListener() {
						@Override
						public void onError(UiError e) {
							if (FileListActivity.this.isFinishing()) {
								return;
							}
							mProgress.dismiss();
							Toast.makeText(getApplicationContext(), "删除文件失败 ：" + e.errorMessage, Toast.LENGTH_LONG).show();
						}
						
						@Override
						public void onComplete(Object obj) {
							if (FileListActivity.this.isFinishing()) {
								return;
							}
							mProgress.dismiss();
							mFileList.remove(position);
							mAdapter.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(), "删除文件成功!", Toast.LENGTH_LONG).show();
						}
						
						@Override
						public void onCancel() {
						}
					});
                }
            }.start();
        }

    };
}
