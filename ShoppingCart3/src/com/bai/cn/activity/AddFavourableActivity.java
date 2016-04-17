package com.bai.cn.activity;

import com.bai.cn.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AddFavourableActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_p);
		initView();
	}

	private void initView(){
	  TextView title =(TextView)findViewById(R.id.title);
	  title.setText("tian jia you  hui jian");
		
	}
}
