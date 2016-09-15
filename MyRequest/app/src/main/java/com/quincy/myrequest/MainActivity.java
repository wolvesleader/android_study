package com.quincy.myrequest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.quincy.myrequest.net.HttpRequest;
import com.quincy.myrequest.net.RequestCallBack;
import com.quincy.myrequest.net.RequestParameter;
import com.quincy.myrequest.net.URLData;


import java.util.TreeMap;

/**
 * 常见的json数据的格式之一
 * ｛
 *      "isError":true,
 *      "errorType":"1",
 *      "erroeMessage":"网络异常",
 *      "result":"请求成功之后的数据"
 *  ｝
 *  常见的json数据的格式之二
 *  ｛
 *      "isError":true
 *      "errorType":"1"
 *      "erroeMessage":"网络异常"
 *      "result":{
 *          "cinemaId":1,
 *          "cinemaName":"星美"
 *      }
 *  ｝
 *  常见的json数据的格式之三
 * ｛
 *      "isError":true
 *      "errorType":"1"
 *      "erroeMessage":"网络异常"
 *      "result":［
 *        { "cinemaId":1, "cinemaName":"星美"},
 *        { "cinemaId":2, "cinemaName":"匡威"}
 *      ］
 *  ｝
 *
 *  问题POST请求的参数是怎么发送到服务器端的，NoHttp是怎么做的
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void net(View view){
        System.out.println("点击请求网络");

        RequestParameter params = new RequestParameter();
        TreeMap<String, String> treeMap = params.treeMap;
        treeMap.put("name","miao");
        treeMap.put("id","10021");
        URLData data = new URLData();
        data.url = "http://www.baidu.com";
        data.netType = "GET";


        HttpRequest request = new HttpRequest(data,params,new RequestCallBack(){

            @Override
            public void onSuccess(String result) {
                System.out.println("网络请求成功了" + result);
            }

            @Override
            public void onFail(String message) {
                System.out.println("网络请求失败了");
            }
        });

        new Thread(request).start();
        /*
        Request<String> request = NoHttp.createStringRequest("namespace", RequestMethod.POST);
        request.add("S","d");
        request.cancel();
        //CallServer.getRequestInstance().add(this, 0, request, httpListener, true, true);
        HttpPost client = new HttpPost();
        client.abort();

       // HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        */

    }
}
