package com.bai.cn.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @Title: MainPagerAdapter.java
 * @Package com.motu.ui.adapter
 * @Description: 主页面的viewpager的适配器
 * @version V1.0
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {


	ArrayList<Fragment> list;

	public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		this.list = list;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
					super.destroyItem(container, position, object);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

}
