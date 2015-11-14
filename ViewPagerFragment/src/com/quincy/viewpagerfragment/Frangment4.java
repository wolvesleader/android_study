package com.quincy.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Frangment4 extends Fragment{

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = View.inflate(getActivity(), R.layout.list_fragment, null);
		TextView textView = (TextView)v.findViewById(R.id.textview);
		textView.setText("Frangment4");
		textView.setTextSize(30);
		return v;
	}
	
}
