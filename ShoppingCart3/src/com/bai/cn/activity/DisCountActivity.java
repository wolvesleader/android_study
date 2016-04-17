package com.bai.cn.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bai.cn.R;
import com.bai.cn.fragment.DiscountFragment;
import com.bai.cn.fragment.PreferentialFragment;


public class DisCountActivity extends FragmentActivity {
	private ViewPager vp_one_capture;
	private RadioButton rb_capture, rb_result;
	private ArrayList<Fragment> mFragments;
	private RadioGroup rg_status_ded;
	private int mSelectIndex = -1;
	private ViewPagerAdapter mBrowseHistoryAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discount_activity);
		initView();
		initViewPager();
	}

	private void initView() {
		vp_one_capture = (ViewPager) findViewById(R.id.vp_one_capture);
		rg_status_ded = (RadioGroup) findViewById(R.id.rg_status_ded);
		rb_capture = (RadioButton) findViewById(R.id.rb_capture);// 折扣
		rb_result = (RadioButton) findViewById(R.id.rb_past_result);// 优惠卷
	}

	private void initViewPager() {
		mFragments = new ArrayList<Fragment>();
		Fragment mDiscountFragment = PreferentialFragment.newInstance();
		Fragment mPreferentialFragMent = DiscountFragment.newInstance();

		mFragments.add(mDiscountFragment);
		mFragments.add(mPreferentialFragMent);
		mBrowseHistoryAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
		vp_one_capture.setAdapter(mBrowseHistoryAdapter);

		vp_one_capture.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						switchTab(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});

		rg_status_ded.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rb_capture:
							switchTab(0);
							break;
						case R.id.rb_past_result:
							switchTab(1);
							break;
						}
					}
				});
		switchTab(vp_one_capture.getCurrentItem());
	}

	public void switchTab(int index) {
		if (mSelectIndex == index)
			return;
		mSelectIndex = index;
		rg_status_ded.check(rg_status_ded.getChildAt(index).getId());
		vp_one_capture.setCurrentItem(index, true);
		mBrowseHistoryAdapter.notifyDataSetChanged();
	}
}
