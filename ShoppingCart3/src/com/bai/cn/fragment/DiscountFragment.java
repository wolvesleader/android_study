package com.bai.cn.fragment;

import com.bai.cn.R;
import com.bai.cn.activity.AddDiscountActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DiscountFragment extends Fragment{

	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.fragement_discount, null);
		initView(view);
		return view;
	}
	public static DiscountFragment newInstance() {
		DiscountFragment df = new DiscountFragment();
        return df;
    }
	private void initView(View view) {
		Button btn= (Button)view.findViewById(R.id.btn_jia);
		btn.setTag("+");
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(getActivity(), AddDiscountActivity.class);
				startActivity(intent);
				
			}
		});

	}
	
}

