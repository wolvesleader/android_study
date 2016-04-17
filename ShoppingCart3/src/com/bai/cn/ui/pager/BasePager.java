package com.bai.cn.ui.pager;

import com.bai.cn.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * 五个子页面对象基类
 * 
 * @author Kevin
 * 
 */
public class BasePager {

	public Activity mActivity;
	public View mRootView;

	public TextView tvTitle;// 页面标题
	public FrameLayout flContent;// 页面内容
	public ImageButton btnMenu;// 菜单按钮
	public ImageButton btnDisplay;//组图切换按钮

	public BasePager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	/**
	 * 初始化布局
	 * 
	 * @return
	 */
	public View initView() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		//tvTitle = (TextView) view.findViewById(R.id.tv_title);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		//btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		//btnDisplay = (ImageButton) view.findViewById(R.id.btn_display);

		

		return view;
	}

	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
