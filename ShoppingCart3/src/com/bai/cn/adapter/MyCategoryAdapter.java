package com.bai.cn.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bai.cn.fragment.FragmentFactory;

public class MyCategoryAdapter extends FragmentPagerAdapter{
	
	private List<String> mDatas;

	public MyCategoryAdapter(FragmentManager fm,List<String> mDatas) {
		super(fm);
		this.mDatas = mDatas;
	}

	@Override
	public Fragment getItem(int position) {
		return FragmentFactory.create(position);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

}

