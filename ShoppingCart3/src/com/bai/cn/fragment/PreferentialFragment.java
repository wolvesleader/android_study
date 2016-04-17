package com.bai.cn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.bai.cn.R;
import com.bai.cn.activity.AddFavourableActivity;

public class PreferentialFragment extends Fragment{

	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.fragment_pp, null);
		initView(view);
		return view;
	}
	public static PreferentialFragment newInstance() {
		PreferentialFragment pf = new PreferentialFragment();
        return pf;
    }
	
	private void initView(View view) {
		Button btn= (Button)view.findViewById(R.id.btn_p);
		btn.setTag("+");
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(getActivity(), AddFavourableActivity.class);
				startActivity(intent);
				
			}
		});

	}
}
