package com.bai.cn.activity;

import com.bai.cn.R;
import com.bai.cn.fragment.CartFragment;
import com.bai.cn.fragment.MyFragment;
import com.bai.cn.fragment.ShopFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		FragmentManager fms = getSupportFragmentManager();
		FragmentTransaction fts = fms.beginTransaction();
		// 替换帧布局
		fts.add(R.id.fl_container, new ShopFragment(fms), "ShopFragment");
		// 提交事务
		fts.commit();
		// fm.findFragmentByTag(arg0); 根据tag获取Fragment对象

		initView();

	}

	private void initView() {

		

		ImageButton iv_shop = (ImageButton) findViewById(R.id.iv_shop1);
		iv_shop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fmc = getSupportFragmentManager();
				FragmentTransaction ftc = fmc.beginTransaction();
				Toast.makeText(HomeActivity.this, "ShopFragment", Toast.LENGTH_LONG)
						.show();
				ftc.replace(R.id.fl_container, new ShopFragment(fmc),
						"ShopFragment");
				// 提交事务
				 ftc.commit();
			}
		});

		ImageButton iv_my = (ImageButton) findViewById(R.id.iv_my);
		iv_my.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fmd = getSupportFragmentManager();
				FragmentTransaction ftd = fmd.beginTransaction();
				Toast.makeText(HomeActivity.this, "MyFragment", Toast.LENGTH_LONG)
						.show();
				ftd.replace(R.id.fl_container, new MyFragment(fmd), "MyFragment");
				// 提交事务
				 ftd.commit();
			}
		});
		
		ImageButton iv_cart = (ImageButton) findViewById(R.id.iv_cart);
		iv_cart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Toast.makeText(HomeActivity.this, "CartFragment", Toast.LENGTH_LONG)
						.show();
				ft.replace(R.id.fl_container, new CartFragment(fm),
						"CartFragment");
				// 提交事务
				 ft.commit();
			}
		});
	}

}
