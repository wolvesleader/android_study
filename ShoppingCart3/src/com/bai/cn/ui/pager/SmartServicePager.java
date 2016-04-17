package com.bai.cn.ui.pager;


import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * 智慧服务
 * 
 * @author Kevin
 * 
 */
public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("智慧服务初始化...");
		//btnMenu.setVisibility(View.VISIBLE);
		//tvTitle.setText("智慧服务");

		TextView view = new TextView(mActivity);
		view.setText("智慧服务");
		view.setTextSize(22);
		view.setTextColor(Color.RED);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);
	}

}
