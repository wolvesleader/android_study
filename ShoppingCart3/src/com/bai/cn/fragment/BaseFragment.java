package com.bai.cn.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类
 * 
 * @author Kevin
 * 
 */
public abstract class BaseFragment extends Fragment {

	public Activity mActivity;

	// Fragment被创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();// 获取所依赖的Activity
	}

	// 初始化Fragment布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView();
		return view;
	}

	// Fragment所依赖的Activity创建完成
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	/**
	 * 初始化布局
	 */
	public abstract View initView();

	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
