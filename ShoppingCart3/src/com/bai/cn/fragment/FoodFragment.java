package com.bai.cn.fragment;

import java.util.ArrayList;

import com.bai.cn.R;
import com.bai.cn.adapter.CategoryAdapter;
import com.bai.cn.domain.Electronic;
import com.bai.cn.domain.Electronic.ElectronicInfo;
import com.bai.cn.utils.HttpHelper;
import com.bai.cn.utils.HttpHelper.OnResultListener2;
import com.bai.cn.utils.JsonUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 食品
 * @author 
 *
 */
public class FoodFragment extends BaseFragment implements OnResultListener2{
	public static final String BUNDLE_TITLE = "title";
	private String mTitle = "DefaultValue";
	
	private View view;
	private ListView lv_electronic;
	ArrayList<ElectronicInfo> lists = new ArrayList<ElectronicInfo>();
	
	@Override
	public View initView() {
		view = View.inflate(getActivity(), R.layout.fragment_electronic, null);
        lv_electronic = (ListView)view.findViewById(R.id.lv_electronic);
		initData();
		return view;
	}
	
	public void initData(){
		/*Map<String, String> map = new HashMap<String, String>();
		map.put("id", orderId);*/
		HttpHelper.post("http://192.168.1.101:8080/bai_food.json", null, this, 0);
	}

	public static ElectronicFragment newInstance(String title)
	{
		Bundle bundle = new Bundle();
		bundle.putString(BUNDLE_TITLE, title);
		ElectronicFragment fragment = new ElectronicFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void getResult(String result, int flag) {
		switch (flag) {
		case 0:
			Electronic electronicInfos = JsonUtil.parseJsonToBean(result, Electronic.class);
		
			CategoryAdapter categoryAdapter = new CategoryAdapter(electronicInfos.allList, getActivity());
			lv_electronic.setAdapter(categoryAdapter);
			categoryAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
}
