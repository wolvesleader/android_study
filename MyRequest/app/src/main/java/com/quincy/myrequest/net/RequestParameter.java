package com.quincy.myrequest.net;

import java.util.TreeMap;

/**
 * Created by quincy on 16/9/3.
 */
public class RequestParameter {


    public TreeMap<String,String> treeMap ;

    public RequestParameter(){
        treeMap = new TreeMap<>();
    }
    //添加String类型的参数
    private void add(String key,String value){
        treeMap.put(key,value);
    }


}
