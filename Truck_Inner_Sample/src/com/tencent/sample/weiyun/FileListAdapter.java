package com.tencent.sample.weiyun;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.sample.R;
import com.tencent.open.weiyun.WeiyunFile;

public class FileListAdapter extends BaseAdapter {
    
    private List<WeiyunFile> mData;
    Context context;
    
    private IFileListAdapterItemClick mListener;
    private int mActiontype;

    //ViewHolder静态类
    static class ViewHolder
    {
        public Button thumButton;
        public Button downButton;
        public Button deleteButton;
        
        public TextView filenameTextView;
        public TextView filectimeTextView;
        public TextView filesizeTextView;
    }
    
    public FileListAdapter(Context ctx, IFileListAdapterItemClick listener) {
        super();
        context = ctx;
        mListener = listener;
    }
    
    public void setData(List<WeiyunFile> list, int actiontype) {
        mData = list;
        mActiontype = actiontype;
    }
    
    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        } else {
            return mData.size();
        }
        
    }

    @Override
    public Object getItem(int arg0) {
        if (mData == null) {
            return null;
        } else {
            return mData.get(arg0);
        }
    }

    @Override
    public long getItemId(int arg0) {
        if (mData == null) {
            return 0;
        } else {
            return arg0;
        }
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        final WeiyunFile infoitem = (WeiyunFile)getItem(arg0);
        if (infoitem == null) {
            return null;
        }
        ViewHolder viewHolder = null;  
        if(arg1 == null){
            arg1 = LayoutInflater.from(context).inflate(R.layout.weiyun_filelist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.thumButton = (Button) arg1.findViewById(R.id.thumbButton);
            viewHolder.downButton = (Button) arg1.findViewById(R.id.downloadButton);
            viewHolder.deleteButton = (Button) arg1.findViewById(R.id.deleteButton);
            viewHolder.filenameTextView = (TextView) arg1.findViewById(R.id.textView_filename);
            viewHolder.filectimeTextView = (TextView) arg1.findViewById(R.id.textView_ctime);
            viewHolder.filesizeTextView = (TextView) arg1.findViewById(R.id.textView_filesize);
            
            arg1.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)arg1.getTag();
        }
        String filename = infoitem.getFileName();
        int filesize = (int)infoitem.getFileSize();
        String filectime = infoitem.getCreateTime();
        viewHolder.filenameTextView.setText("文件名:   " + filename + "");
        viewHolder.filectimeTextView.setText("创建时间:  " + filectime + "");
        viewHolder.filesizeTextView.setText("文件大小:  " + filesize + " 字节");
        viewHolder.thumButton.setOnClickListener(new lvButtonListener(arg0));
        viewHolder.downButton.setOnClickListener(new lvButtonListener(arg0));
        viewHolder.deleteButton.setOnClickListener(new lvButtonListener(arg0));

        if (mActiontype == WeiyunMainActivity.ACTION_PICTURE) {
            viewHolder.thumButton.setVisibility(View.VISIBLE);
        } else {
            viewHolder.thumButton.setVisibility(View.GONE);
        }
        //viewHolder.deleteButton.setVisibility(View.GONE);
        
        return arg1;
    }

    class lvButtonListener implements OnClickListener {
        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }
        
        @Override
        public void onClick(View v) {
            int vid=v.getId();
            if (vid == R.id.thumbButton) {
                mListener.onThumbPicClick(position);
            } else if (vid == R.id.downloadButton) {
                mListener.onDownloadClick(position, mActiontype);
            } else if (vid == R.id.deleteButton) {
                mListener.onDeleteClick(position, mActiontype);
            }
        }
    }
}
