package com.bai.cn.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bai.cn.R;
import com.bai.cn.adapter.MyCategoryAdapter;
import com.bai.cn.view.ViewPagerIndicator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ShopFragment extends Fragment{
	
	private List<Fragment> mTabContents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private com.bai.cn.view.MyViewPager mViewPager;
	private List<String> mDatas = Arrays.asList("电子", "食品", "日用品", "酒类");

	private ViewPagerIndicator mIndicator; 

	private FragmentManager fm;
	private View view;
	
	public ShopFragment(FragmentManager fm){
		this.fm = fm;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = View.inflate(getActivity(), R.layout.vp_indicator, null);
		initView();
		//initDatas();
		//设置Tab上的标题
		
		
		return view;
	}
	
	


	private void initView()
	{
		mViewPager = (com.bai.cn.view.MyViewPager)view. findViewById(R.id.id_vp);
		mIndicator = (ViewPagerIndicator) view.findViewById(R.id.id_indicator);
		mIndicator.setTabItemTitles(mDatas);
		mViewPager.setAdapter(new MyCategoryAdapter(fm,mDatas));
		//设置关联的ViewPager
		mIndicator.setViewPager(mViewPager,0);
	}

}
