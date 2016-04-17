package com.bai.cn.adapter;

import java.util.ArrayList;
import java.util.Date;

import com.bai.cn.R;
import com.bai.cn.db.CartDao;
import com.bai.cn.domain.Cart;
import com.bai.cn.domain.Electronic;
import com.bai.cn.domain.Electronic.ElectronicInfo;
import com.bai.cn.utils.IdUtil;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryAdapter extends BaseAdapter{
	
	private ArrayList<ElectronicInfo> electronicList;
	private Context ctx;
	
	public CategoryAdapter(ArrayList<ElectronicInfo> electronicList,Context ctx){
		this.electronicList = electronicList;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return electronicList == null? 0 :electronicList.size();
	}

	@Override
	public ElectronicInfo getItem(int position) {
		return electronicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(ctx, R.layout.item_category, null);
			
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.price = (TextView) convertView.findViewById(R.id.tv_price);
			holder.add = (ImageView) convertView.findViewById(R.id.iv_add);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final ElectronicInfo info = electronicList.get(position);
		holder.name.setText(info.name);
		holder.price.setText(info.price);
		
		
		holder.add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(ctx, "添加到购物车", 1000).show();
				CartDao cartDao = CartDao.getInstance(ctx);
				Cart findInfo = cartDao.findByName(info.name);
				if(findInfo == null){
					cartDao.add(IdUtil.getString(new Date()), info.name, info.price, 0+"", null);
				}else{
					int	count = Integer.parseInt(findInfo.count) + 1;
					cartDao.add(IdUtil.getString(new Date()), info.name, info.price, count+"", null);
				}
				
			}
		});
		
		return convertView;
	}
	
	
	static class ViewHolder{
		public TextView name;
		public TextView price;
		public ImageView add;
	}
	
	

}
