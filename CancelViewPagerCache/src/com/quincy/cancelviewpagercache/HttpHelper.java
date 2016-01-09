package com.quincy.cancelviewpagercache;


import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 网络连接的帮助类
 * @author quincy
 *
 */
public class HttpHelper {

	//public static final HttpUtils http = new HttpUtils(5000);//  5s超时
	
	
	/**
	 * 不带参数的请求方式
	 * @param url
	 * @param listener
	 */
	public static void get(String url,final OnResultListener listener){
		HttpUtils http = new HttpUtils(5000);//  5s超时
		http.configCurrentHttpCacheExpiry(0);   //  设置缓存0 
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				if(listener != null){
					listener.getResult(responseInfo.result);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
				if(listener != null){
					listener.getResult(msg);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}
		});
		
	}
	
	/**
	 * 携带有参数的请求
	 * @param url
	 * @param params
	 * @param listener
	 */
	public static void get(String url,RequestParams params,final OnResultListener listener){
		HttpUtils http = new HttpUtils(5000);
		http.configCurrentHttpCacheExpiry(0);   //  设置缓存0 
		http.send(HttpMethod.GET, url,params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if(listener != null){
					listener.getResult(responseInfo.result);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if(listener != null){
					listener.getResult(msg);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}
		});
		
	}
	
	
	/**
	 * 携带有参数的请求
	 * @param url
	 * @param params
	 * @param listener
	 * @param  flag//同一个类中需要执行多次该方法，可以通过flag标识不同的执行返回不同的结果
	 *如果想设置自己的http参数怎么办，在重载一个这个方法就ok了
	 */
	public static void post(String url,Map<?, ?> map,final OnResultListener2 listener,final int flag){
		HttpUtils https = new HttpUtils();
		https.configCurrentHttpCacheExpiry(0);   //  设置缓存0 
		RequestParams params = HttpHelper.setRequestParams(map);
		https.send(HttpMethod.POST, url,params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if(listener != null){
					listener.getResult(responseInfo.result,flag);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//失败的时候返回字符串999999
				if(listener != null){
					listener.getResult("获取数据失败",999999);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}
		});
		
	}
	
	/**
	 * 
	 * @param http
	 * @param url
	 * @param map
	 * @param listener
	 * @param flag
	 */
	public static void post(HttpUtils http,String url,Map<?, ?> map,final OnResultListener2 listener,final int flag){
		
		http.configCurrentHttpCacheExpiry(0);   //  设置缓存0 
		RequestParams params = HttpHelper.setRequestParams(map);
		http.send(HttpMethod.POST, url,params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if(listener != null){
					listener.getResult(responseInfo.result,flag);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//失败的时候返回字符串999999
				if(listener != null){
					listener.getResult("获取数据失败",999999);
				}
			}

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
			}
		});
		
	}
	
	
	
	public interface OnResultListener{
		public abstract void getResult(String result);
	}
	
	public interface OnResultListener2{
		public abstract void getResult(String result,int flag);
	}
	
	
	public static RequestParams setRequestParams(Map<?, ?> map){
		RequestParams params = new RequestParams();
		String json = JsonUtil.parseMapToJson(map);
		params.addBodyParameter("jsondata", json);
		return params;
	}
	
}
