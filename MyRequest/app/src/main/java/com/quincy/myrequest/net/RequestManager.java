package com.quincy.myrequest.net;

import java.util.ArrayList;

/**
 * Created by quincy on 16/9/3.
 * 管理网络请求
 */
public class RequestManager {

    ArrayList<HttpRequest> requestlist = null;
    //需要研究一下采用那种存储比较节省时间
    public RequestManager(){
        requestlist = new ArrayList<>();
    }

    /**
     * 添加请求
     * 在什么时候调用该方法把请求添加到请求列表中的
     * 在RequestManager中的createRequest方法中，创建请求的时候把创建好的请求添加到请求队列中
     */
    private void addRequest(HttpRequest request){
        requestlist.add(request);
    }

    /**
     * 取消请求
     */
    private void cancelRequest(){
        if(requestlist != null && requestlist.size() > 0){
            for (HttpRequest request: requestlist) {

            }
        }
    }

}
