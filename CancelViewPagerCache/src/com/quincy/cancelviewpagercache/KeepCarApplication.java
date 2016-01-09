package com.quincy.cancelviewpagercache;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class KeepCarApplication extends Application{
	
	 private static Context context;
	 private static Handler mainHandler;//主线程的handler
	 
	  public  double latitude = 1000;
	  public  double longitude = 1000;
	  public String curAddr,cityCode,city,provice;
	 
	  
	  private static KeepCarApplication mApp = null;
	
	  
	  public static KeepCarApplication getApp(){
		    return mApp;
     }
	  
	  
	  @Override
	public void onCreate() {
		super.onCreate();
		context = this;
		mApp=this;
	
	    
	}
	  
	
	
		
	  public static Context getContext(){
			return context;
	  }
	  public static Handler getHandler(){
			return mainHandler;
	  }
	 
		/**
		 * 在主线程中更新UI
		 * @param runnable
		 */
	   public static void runOnUIThread(Runnable runnable){
		   getHandler().post(runnable);
	   }
	
  
}
