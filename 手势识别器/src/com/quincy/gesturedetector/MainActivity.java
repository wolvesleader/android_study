package com.quincy.gesturedetector;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 
    按下（onDown）： 刚刚手指接触到触摸屏的那一刹那，就是触的那一下。
    抛掷（onFling）： 手指在触摸屏上迅速移动，并松开的动作。
    长按（onLongPress）： 手指按在持续一段时间，并且没有松开。
    滚动（onScroll）： 手指在触摸屏上滑动。
    按住（onShowPress）： 手指按在触摸屏上，它的时间范围在按下起效，在长按之前。
    抬起（onSingleTapUp）：手指离开触摸屏的那一刹那。
    任何手势动作都会先执行一次按下（onDown）动作。
    长按（onLongPress）动作前一定会执行一次按住（onShowPress）动作。
    按住（onShowPress）动作和按下（onDown）动作之后都会执行一次抬起（onSingleTapUp）动作。
    长按（onLongPress）、滚动（onScroll）和抛掷（onFling）动作之后都不会执行抬起（onSingleTapUp）动作。
 * @author quincy
 *
 *一般使用实现的的继承接口
 *SimpleOnGestureListener  
 *OnGestureListener
 *OnDoubleTapListener
 *
 *相似的手势类
 *ScaleGestureDetector，两手指按下，图片的缩放
 */

public class MainActivity extends Activity implements OnTouchListener,
		OnGestureListener {

	@SuppressWarnings("deprecation")
	private GestureDetector detector = new GestureDetector(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView image = (ImageView)findViewById(R.id.image);
		image.setOnTouchListener(this);
	}

	// OnGestureListener
	@Override
	public boolean onDown(MotionEvent e) {
		
		System.out.println("&&&&&onDown");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		System.out.println("&&&&&onFling");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		System.out.println("&&&&&onLongPress");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		System.out.println("&&&&&onScroll");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		System.out.println("&&&&&onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		System.out.println("&&&&&onSingleTapUp");
		return false;
	}
	
	

	// OnGestureListener//

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		detector.onTouchEvent(event);//把手势交给手势识别器去判断
		//return false;//返回false不能够接收到手指的滑动事件
		return true;
	}

}
