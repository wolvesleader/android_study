package com.quincy.setcolorforlistview;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 点击ListView设置背景的颜色，
 * 问题的一般在复用了ListView之后当给其中的一个设置颜色之后，在复用的的也会被设置上颜色
 * 这里提供几种解决方案
 * 1，如果数据不是太多就不要使用ListView的复用规则了
 * 2，给ListView的每一个javabean添加一个boolean变量
 * 这种思想可以解决一系列由于listView的复用引起的问题，比如listview中的checkbox，radiobutton等等
 * @author quincy
 *这个demo使用xutils的框架需要导入jar包
 */
public class MainActivity extends Activity {

	private ListView listview;
	private ArrayList<TestBeanInfo> lists = new ArrayList<TestBeanInfo>();
	private ArrayList<String> ids = new ArrayList<String>();//可以把选中的id都存起来，便于以后做操作
	private TestAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		listview = (ListView)findViewById(R.id.listview);
		adapter = new TestAdapter();
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_textview,str);
		listview.setAdapter(adapter);
		//getDataFromServer();
		getDataFromLocation();
	}
	
	//从本地获取测试数据
	private void getDataFromLocation(){
		try {
			InputStream stream = getResources().getAssets().open("listview.json");
			String result = streamToString(stream);
			parseData(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把读取的文件流解析为字符串，主要是为了获取到本地的测试数据
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public  String streamToString(InputStream in) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int len = 0;
		byte[] buffer = new byte[1024];

		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}

		String result = out.toString();

		in.close();
		out.close();

		return result;
	}
	
	
	/**
	 * 从服务器中获取测试数据
	 * listview.json//测试数据
	 */
	private void getDataFromServer(){
		HttpUtils http = new HttpUtils();
		
		RequestParams params = new RequestParams();
		http.send(HttpMethod.GET,"http://192.168.1.73:8080/listview.json",params, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String result = responseInfo.result;
					parseData(result);
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					System.out.println("jkjkjk");
				}
			});
	}
	

	private void parseData(String result){
		TestBean bean = JsonUtil.parseJsonToBean(result, TestBean.class);
		lists.addAll(bean.body);
		adapter.notifyDataSetChanged();
		
	}
	
	class TestBean{
		ArrayList<TestBeanInfo> body;
		
	}
	//如果服务器端返回了判断选中的标志，直接封装就额可以了。如果没有怎么办？
	class TestBeanInfo{
		public boolean isColor;
		public String name;
		
		@Override
		public String toString() {
			return "TestBeanInfo [isColor=" + isColor + ", name=" + name + "]";
		}
		
	}
	
	class TestAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			return lists == null?0:lists.size();
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			//textview
			final ViewHolder holder ;
			if(convertView == null){
				convertView = View.inflate(MainActivity.this, R.layout.list_textview, null);
				holder = new ViewHolder();
				holder.textview = (TextView)convertView.findViewById(R.id.textview);
				holder.ll_container = (LinearLayout)convertView.findViewById(R.id.ll_container);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			final TestBeanInfo info = lists.get(position);
			
			holder.textview.setText(info.name);
			holder.ll_container.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(!info.isColor){
						holder.ll_container.setBackgroundColor(getResources().getColor(R.color.maintain));
						info.isColor = true;
						ids.add(String.valueOf(position));//添加到集合中
					}else{
						info.isColor = false;
						holder.ll_container.setBackgroundColor(Color.TRANSPARENT); 
						ids.remove(String.valueOf(position));//从集合中删除
					}
				}
				
			});
			
			if(info.isColor){
				info.isColor = true;
				holder.ll_container.setBackgroundColor(getResources().getColor(R.color.maintain));
			}else{
				holder.ll_container.setBackgroundColor(Color.TRANSPARENT);      
			}
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		public TextView textview;
		public LinearLayout ll_container; 
	}

}
