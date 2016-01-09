package com.quincy.cancelviewpagercache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.quincy.cancelviewpagercache.CityChoose.CityList;
import com.quincy.cancelviewpagercache.HttpHelper.OnResultListener2;
import com.quincy.cancelviewpagercache.MyViewPager.OnPageChangeListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;



public class CityChooseActivity extends BaseFragmentActivity implements OnResultListener2{

	private TextView toolsTextViews[];
	private View views[];
	private LayoutInflater inflater;
	private ScrollView scrollView;
	private int scrllViewWidth = 0, scrollViewMiddle = 0;
	private MyViewPager shop_pager;
	private int currentItem=0;
	private CityChooseAdapter shopAdapter;
	private LinearLayout toolsLayout;
	private ArrayList<CityList> arrayList = new ArrayList<CityList>();
	//public static CityList fff;
	
	//点击第几个列表
	
	
	private View.OnClickListener toolsItemListener =new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//fff = arrayList.get(v.getId());
			//ToastUtil.showToast(v.getId() + "--" + arrayList.get(v.getId()).city);
			shop_pager.setCurrentItem(v.getId(),false);
		}
	};
	
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			//CityChooseFragment fragmenssst = (CityChooseFragment)shopAdapter.getItem(arg0);
			//fragmenssst.initView();
			//fragmenssst.chooseCity();
			
			if(shop_pager.getCurrentItem()!=arg0)shop_pager.setCurrentItem(arg0,false);
			if(currentItem!=arg0){
				changeTextColor(arg0);
				changeTextLocation(arg0);
			}
			currentItem=arg0;
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	
	
	private class CityChooseAdapter extends FragmentPagerAdapter {
		private CityChooseFragment fragment;

		public CityChooseAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			
			System.out.println("---------------" + "position " + position);
			
			//ToastUtil.showToast(position + "");
			fragment = new CityChooseFragment();
			//fragment.chooseCity();
			Bundle bundle = new Bundle();
			bundle.putSerializable("obj", arrayList.get(position));
			
			bundle.putString("pos",arrayList.get(position).unid);
			
			fragment.setArguments(bundle);
			return fragment;
		}
		
		
		
		@Override
		public int getCount() {
			return arrayList == null ? 0: arrayList.size();
		}
	}
	
	
	/**
	 * @param id
	 */
	private void changeTextColor(int id) {
		for (int i = 0; i < toolsTextViews.length; i++) {
			if(i!=id){
				toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
				toolsTextViews[i].setTextColor(0xff0596d9);
			}
		}
		toolsTextViews[id].setBackgroundResource(R.drawable.pjfl_leftbg);
		toolsTextViews[id].setTextColor(0xffffffff);
	}
	
	
	/**
	 * 
	 * @param clickPosition
	 */
	private void changeTextLocation(int clickPosition) {
		
		int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
		scrollView.smoothScrollTo(0, x);
	}
	
	/**
	 * 
	 * @return
	 */
	private int getScrollViewMiddle() {
		if (scrollViewMiddle == 0)
			scrollViewMiddle = getScrollViewheight() / 2;
		return scrollViewMiddle;
	}
	
	/**
	 * 
	 * @return
	 */
	private int getScrollViewheight() {
		if (scrllViewWidth == 0)
			scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
		return scrllViewWidth;
	}
	
	/**
	 * 
	 * @param view
	 * @return
	 */
	private int getViewheight(View view) {
		return view.getBottom() - view.getTop();
	}


	@Override
	public View initView() {
		//title.setText("选择城市");
		
		
		View view = View.inflate(this, R.layout.activity_accessory, null);
		scrollView = (ScrollView)view.findViewById(R.id.tools_scrlllview);
		toolsLayout = (LinearLayout)view.findViewById(R.id.tools);
		inflater=LayoutInflater.from(this);
		//showToolsView();
		shop_pager=(MyViewPager)view.findViewById(R.id.goods_pager);
		
		
		return view;
	}

	private void showToolsView(ArrayList<CityList> list) {
		
		int length = list == null ? 0: list.size();
		toolsTextViews=new TextView[length];
		views=new View[length];
		for (int i = 0; i < length; i++) {
			View view=inflater.inflate(R.layout.item_b_top_nav_layout, null);
			view.setId(i);
			
			view.setOnClickListener(toolsItemListener);
			TextView textView=(TextView) view.findViewById(R.id.text);
			textView.setText(list.get(i).city);
			toolsLayout.addView(view);
			toolsTextViews[i]=textView;
			views[i]=view;
		}
		changeTextColor(0);
	}
	
	@Override
	public void initData() {
		showPageLoading();
		chooseProvince();
	}
	
	private void chooseProvince(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentid", "0");
		HttpHelper.post(GlobalContants.addressInfo_URL, map, this, 0);
	}

	@Override
	public void getResult(String result, int flag) {
		parseData(result,flag);
	}

	private void parseData(String result, int flag) {
		switch (flag) {
		case 0:
			
			CityChoose citys = JsonUtil.parseJsonToBean(result, CityChoose.class);
			arrayList.clear();
			arrayList.addAll(citys.allList);
			//fff = arrayList.get(0);//这么写是在太糟糕了
			showToolsView(arrayList);
			shopAdapter=new CityChooseAdapter(getSupportFragmentManager());
			
			//System.out.println(fragmenssst + "---------------");
			shop_pager.setAdapter(shopAdapter);	
			shop_pager.setOnPageChangeListener(onPageChangeListener);
			//fragmenssst.chooseCity(this);
			
			allInvisible();
			break;
		case 999999:
			
			break;
		default:
			break;
		}
	}
	
	
}
