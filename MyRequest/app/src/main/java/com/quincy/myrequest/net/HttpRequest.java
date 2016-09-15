package com.quincy.myrequest.net;

import android.os.Handler;

import com.quincy.myrequest.net.exception.RequestMethodException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by quincy on 16/9/3.
 * 通过URLConnection发起请求
 * 怎么判断一个网络请求比较块而且比较节省流量
 * 高并发请求的设计
 */
public class HttpRequest implements Runnable{


    private HttpURLConnection connection;
    private InputStream inputStream;
    private URLData data ;
    //private List<RequestParameter> params;
    private RequestParameter params;
    private RequestCallBack callBack;
    private Handler handler ;


    //提供无参构造器
    public HttpRequest(final URLData data, final /*List<RequestParameter> params*/RequestParameter params,
                       final RequestCallBack callBack){
        this.callBack = callBack;
        this.params = params;
        this.data = data;
        this.handler = new Handler();
    }

    //异步请求网络数据
    @Override
    public void run() {
        //需要先判断是get请求还是post请求
        request();
    }


    //请求方式
    private void request(){
        if(("get").equals(data.netType.toLowerCase() )){
            getRequest();
        }else if (("post").equals(data.netType.toLowerCase() )){
            postRequest();
        }else{
            throw  new RequestMethodException("不支持的请求方式");
        }
    }

    /**
     * POST请求
     */
    private void postRequest(){
        //需要监测传入的请求参数是否符合要求
        //如果不符合要求，不发起请求，直接返回为null
        try {
            URL url = new URL(data.url);
            connection = (HttpURLConnection)url.openConnection();
            //设置请求方式，post还是get请求
            connection.setRequestMethod(data.netType);

            connection.connect();

            //获取到请求结果的响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200){//响应码是200表示请求成功
                //获取到输出流
                inputStream = connection.getInputStream();
                inputStreamToString(inputStream);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * GET请求
     */
    private void getRequest(){
        //需要监测传入的请求参数是否符合要求
        //如果不符合要求，不发起请求，直接返回为null
        try {
            String requestUrlAndParameter = getRequestUrlAndParameter();
            System.out.println(requestUrlAndParameter + "===========");
            URL url = new URL(requestUrlAndParameter);
            connection = (HttpURLConnection)url.openConnection();
            //设置请求方式，post还是get请求
            connection.setRequestMethod(data.netType);
            connection.connect();
            //获取到请求结果的响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200){//响应码是200表示请求成功
                //获取到输出流
                inputStream = connection.getInputStream();
                final String result = inputStreamToString(inputStream);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        HttpRequest.this.callBack
                                .onSuccess(result);
                    }

                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            handleNetworkError("网络异常");
        } catch (IOException e) {
            e.printStackTrace();
            handleNetworkError("网络异常");
        }
    }

    /**
     * www.baidu.com?type=1&name=quincy&password=miao
     * @return
     */
    private String getRequestUrlAndParameter(){
        String url = data.url;
        StringBuilder urlBuilder = new StringBuilder(url);
        //含有参数
        if (params != null && params.treeMap.size() > 0){
            TreeMap<String, String> treeMap = params.treeMap;
            for (Map.Entry<String, String> item: treeMap.entrySet()) {
                if (urlBuilder.toString().contains("?") && urlBuilder.toString().contains("=") && urlBuilder.toString().length() > 0){
                    urlBuilder.append("&").append(item.getKey()).append("=").append(item.getValue());
                }else if (urlBuilder.toString().length() > 0 && !urlBuilder.toString().endsWith("?")) // end with '?', not append '?'.
                {
                    urlBuilder.append("?").append(item.getKey()).append("=").append(item.getValue());
                }
            }
        }
        return urlBuilder.toString();
    }


    /**
     * 把输出流转换为字符串
     * @param inputStream
     * @return
     */
    private String inputStreamToString(InputStream inputStream){
        //监测输入流是否为空
        if (inputStream == null) return null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1){
                bos.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null){
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return bos != null ? bos.toString() : null;
    }

    //取消请求
    private boolean cancelRequest(){
        if (connection != null){
            connection.disconnect();
        }
        if (inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                return true;
            }
        }
        return false;

    }

    public void handleNetworkError(final String errorMsg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HttpRequest.this.callBack.onFail(errorMsg);
            }
        });
    }

}
