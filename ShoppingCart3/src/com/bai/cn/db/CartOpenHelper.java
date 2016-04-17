package com.bai.cn.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 
 * @author quincy
 *
 */
public class CartOpenHelper extends SQLiteOpenHelper {

	public CartOpenHelper(Context context) {
		super(context, "cart.db", null, 1);
	}

	// 数据库第一次创建时调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists t_cart(id varchar(50) PRIMARY KEY ,name varchar(50) ,price varchar(50) ,count varchar(10),discount varchar(50));";
		db.execSQL(sql);
	}

	// 数据库更新时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
