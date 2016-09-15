package com.quincy.myrequest.net;

/**
 * Created by quincy on 16/9/3.
 * 提供两个回调接口网络请求成功和网络请求失败时候的回调接口
 */
public interface RequestCallBack {

    public abstract void onSuccess(String result);
    public abstract void onFail(String message);


}
