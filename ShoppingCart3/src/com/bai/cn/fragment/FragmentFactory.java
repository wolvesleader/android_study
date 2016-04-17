package com.bai.cn.fragment;

import java.util.HashMap;

import android.support.v4.app.Fragment;

public class FragmentFactory {
	private static HashMap<Integer, Fragment> fragmentCache = new HashMap<Integer, Fragment>();
	/**
	 * 根据位置获取对应位置的fragment对象
	 * @param position
	 * @return
	 */
	public static Fragment create(int position){
		Fragment fragment = fragmentCache.get(position);
		if(fragment==null){
			switch (position) {
			case 0:
				fragment = new ElectronicFragment();
				break;
			case 1:
				fragment = new FoodFragment();
				break;
			case 2:
				fragment = new CommoditiesFragment();
				break;
			case 3:
				fragment = new WineFragment();
				break;
			
			}
			//创建完成存入map
			fragmentCache.put(position, fragment);
		}
		
		return fragment;
	}
}
