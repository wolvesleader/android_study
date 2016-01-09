package com.quincy.cancelviewpagercache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.quincy.cancelviewpagercache.CityChoose.CityList;
import com.quincy.cancelviewpagercache.HttpHelper.OnResultListener2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;




public class CityChooseFragment extends Fragment implements OnResultListener2 {

	private ScrollView scrollView;
	private FlowLayout flowLayout;
	private ArrayList<CityList> cityobjs; 
	private String pos;
	private ArrayList<CityList> arrayList = new ArrayList<CityList>();

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		obj = (CityList) getArguments().get("obj");
		System.out.println("---------------" + "  onCreateView  " + obj);
		
		cityobjs = (ArrayList<CityList>) getArguments().get("cityobjs");
		final String strname = (String) getArguments().get("typename");
		chooseCity();
		scrollView = new ScrollView(getActivity());
		
		flowLayout = new FlowLayout(getActivity());
		flowLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		flowLayout.setPadding(10, 10, 10, 10);
		scrollView.addView(flowLayout);
		
		return scrollView;
	}
	
	
	private CityList obj;

	public void chooseCity() {
		
		pos =  (String)getArguments().get("pos");
		Map<String, String> map = new HashMap<String, String>();
		map.put("parentid", pos);
		System.out.println(getActivity() + "&&&&&&&&&&&&&&");
		Toast.makeText(getActivity(), obj.city, Toast.LENGTH_LONG).show();
		HttpHelper.post(GlobalContants.addressInfo_URL, map, this, 0);
	}

	@Override
	public void getResult(String result, int flag) {
		parseData(result, flag);
	}

	private void parseData(String result, int flag) {
		switch (flag) {
		case 0:

			CityChoose citys = JsonUtil.parseJsonToBean(result,CityChoose.class);
			arrayList.clear();
			CityList ttt = new CityChoose().new CityList();
			ttt.city = "全部";
			arrayList.add(0, ttt);
			arrayList.addAll(citys.allList);
			
					for (int i = 0; i < arrayList.size(); i++) {
						final TextView textView = new TextView(getActivity());
						textView.setGravity(Gravity.CENTER);
						textView.setText(arrayList.get(i).city);
						textView.setTextColor(Color.BLACK);
						textView.setPadding(10, 5, 10, 5);
						final int vv = i;
						textView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
								String choose = textView.getText().toString().trim();
								if("全部".equals(choose)){//传入的是省的id和名字
									//intent.putExtra("choosePid", CityChooseActivity.fff.unid);
									//intent.putExtra("choosePName", CityChooseActivity.fff.city);
								}else{
									intent.putExtra("choosePid", arrayList.get(vv).unid);
									intent.putExtra("choosePName", choose);
								}
								
								//getActivity().setResult(2, intent);
								//getActivity().finish();
							}
						});
						flowLayout.addView(textView);
						
					}
					scrollView.invalidate();
					

			break;
		case 999999:
			//ToastUtil.showToast("请求失败");
		default:
			break;
		}
	}

}
