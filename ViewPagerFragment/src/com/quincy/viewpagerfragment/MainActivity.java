package com.quincy.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * 主要的解决方案：
 * 拦截返回键，通过判断当前的ViewPager来实现
 * @author quincy
 *
 */
public class MainActivity extends FragmentActivity {

	private ViewPager viewpager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewpager = (ViewPager)findViewById(R.id.viewpager);
		FragmentManager manager = getSupportFragmentManager();
		
		viewpager.setAdapter(new TextFragmentAdapter(manager));
		
	}
	
	@Override
	public void onBackPressed() {
		int item = viewpager.getCurrentItem();
		if(item != 0){
			viewpager.setCurrentItem(item - 1);
		}else{
			super.onBackPressed();
		}
	}
	
	public Fragment factoryFragment(int position){
		
		Fragment fragment = null;
		
		switch (position) {
		case 0:
			fragment = new Frangment1();
			break;
		case 1:
			fragment = new Frangment2();
			break;
		case 2:
			fragment = new Frangment3();
			break;
		case 3:
			fragment = new Frangment4();
			break;

		default:
			break;
		}
		return fragment;
	}
	
	
	class TextFragmentAdapter extends FragmentPagerAdapter{

		public TextFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			return factoryFragment(position);
		}

		@Override
		public int getCount() {
			return 4;
		}
		
		
		
	}

}
