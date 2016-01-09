package com.quincy.cancelviewpagercache;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;




/**
 * 
 * @author quincy
 *
 */
public abstract class BaseFragmentActivity extends FragmentActivity{
	//在基类中需要需要统一填写网络请求，为空的布局文件
	public FrameLayout fl_container;//让他的子类来添加布局文件
	public TextView topbar;
	public RelativeLayout page_empty,page_error;
	public FrameLayout page_loading;
	public Button btn_reload;
	
	
	public RelativeLayout head;
	public TextView title;
	public ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		page_empty = (RelativeLayout)findViewById(R.id.page_empty);
		page_error = (RelativeLayout)findViewById(R.id.page_error);
		page_loading = (FrameLayout)findViewById(R.id.page_loading);
		fl_container = (FrameLayout)findViewById(R.id.fl_container);
		head = (RelativeLayout) findViewById(R.id.rl_tophead_root);
		head.setBackgroundColor(getResources().getColor(R.color.quick_find_top));
		back = (ImageButton) findViewById(R.id.head_button_01);
		//back.setImageResource(R.drawable.back);
		title = (TextView) findViewById(R.id.head_title);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_reload = (Button) findViewById(R.id.btn_reload);
		btn_reload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initData();
			}
		});
		
		View view = initView();
		
		fl_container.addView(view);
		
	
		//请求数据的时候就需要检测
		initData();
	}
	
	public abstract View initView();
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		
	}
	
	/**
	 * 显示空页面
	 */
	public void showPageEmpty(){
		page_empty.setVisibility(View.VISIBLE);
		page_loading.setVisibility(View.GONE);
		page_error.setVisibility(View.GONE);
	}
	/**
	 * 显示错误页面
	 */
	public void showPageError(){
		page_error.setVisibility(View.VISIBLE);
		page_empty.setVisibility(View.GONE);
		page_loading.setVisibility(View.GONE);
		
	}
	
	/**
	 * 显示加载页面
	 */
	public void showPageLoading(){
		page_empty.setVisibility(View.GONE);
		page_loading.setVisibility(View.VISIBLE);
		page_error.setVisibility(View.GONE);
	}
	
	/**
	 * 全部隐藏
	 */
	public void allInvisible(){
		page_empty.setVisibility(View.GONE);
		page_loading.setVisibility(View.GONE);
		page_error.setVisibility(View.GONE);
	}
	
}
