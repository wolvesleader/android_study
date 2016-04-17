package com.bai.cn.ui.pager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.bai.cn.R;
import com.bai.cn.adapter.MyCategoryAdapter;
import com.bai.cn.fragment.CommoditiesFragment;
import com.bai.cn.fragment.ElectronicFragment;
import com.bai.cn.fragment.FoodFragment;
import com.bai.cn.fragment.WineFragment;
import com.bai.cn.view.MyViewPager;
import com.bai.cn.view.ViewPagerIndicator;


/**
 * 首页
 * 
 * @author bai
 * 
 */
public class HomePager extends BasePager {
	
	private MyViewPager mViewPager;
	private List<String> mDatas = Arrays.asList("电子", "食品", "日用品", "酒类");
	private ViewPagerIndicator mIndicator;
	private View view;
	private FragmentManager manager;
	private ArrayList<Fragment> fff = new ArrayList<Fragment>();

	public HomePager(Activity activity,FragmentManager manager) {
		super(activity);
		this.manager = manager;
	}
	
	


	@Override
	public void initData() {
		System.out.println("首页初始化...");
		view = View.inflate(mActivity, R.layout.vp_indicator, null);

		init();
		// initDatas();
		// 设置Tab上的标题
		mIndicator.setTabItemTitles(mDatas);
		
		// 设置关联的ViewPager
		mIndicator.setViewPager(mViewPager, 0);
		MyCategoryAdapter adapter = new MyCategoryAdapter(manager, mDatas);
		mViewPager.setAdapter(adapter);
		
		flContent.addView(view);
		
	}
	
	
	private void init() {
		mViewPager = (MyViewPager)view. findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicator)view. findViewById(R.id.id_indicator);
	}
	
	

}
