package com.quincy.keepingcar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 测试微信登录的demo,分享的demo
 * 
 * @author quincy
 * 
 */
public class MainActivity extends Activity {

	private WXShareHelper wxShareHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		wxShareHelper = new WXShareHelper(this);
		wxShareHelper.setmType(1);
		//wxShareHelper.shareText("测试微信分享文字");
		//你的图片路径.startsWith("http://") || 你的图片路径.startsWith("https://")
		//wxShareHelper.shareImg("http://pic.autobobo.com//repair//425204a0-9008-4ef3-a85c-c0ddaa74485f.jpg", 100);
		wxShareHelper.shareWebPage("http://j.map.baidu.com/pF-t7", false, "http://pic.autobobo.com//repair//425204a0-9008-4ef3-a85c-c0ddaa74485f.jpg", "烩车网，致力于国内的汽车后市场的平台，提供各类汽车相关的服务，欢迎商家入驻我们的网站，同时欢迎各位车主，下载我们的APP,如您在使用的过程中发现不好用的挥着用着不顺手的地方，请您及时的联系我们。也可以直接拨打我的电话15709686612，忘记介绍了，我是烩车网的一枚软件工程师", "这是一个神奇的网站", 100);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (wxShareHelper != null)
			wxShareHelper.onNewIntent(intent);
	}

}
