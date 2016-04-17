package com.bai.cn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdUtil {

	public static String getString(Date date){
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	      return sdf.format(date);
	 }
}
