package com.bai.cn.db;

import java.util.ArrayList;

import com.bai.cn.domain.Cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * @author bai
 * 
 * 数据库里边的数据发生了变化需要通知从新去更新数据
 * 
 */
public class CartDao {

	private static CartDao sInstance = null;// 懒汉模式
	private CartOpenHelper mHelper;

	private CartDao(Context ctx) {
		mHelper = new CartOpenHelper(ctx);
	}

	public static CartDao getInstance(Context ctx) {
		if (sInstance == null) {
			// A, B
			synchronized (CartDao.class) {
				if (sInstance == null) {
					sInstance = new CartDao(ctx);
				}
			}
		}

		return sInstance;
	}
	
	/**
	 * 添加购物车数据
	 */
	public void add(String id, String name,String price,String count,String discount) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put("id", id);
		values.put("name", name);
		values.put("price", price);
		values.put("count", count);
		values.put("discount", discount);
		//values.put("coupon", A_Store);
		database.insert("bst", null, values);
		database.close();
	}

	/**
	 *
	 * 
	 * @param id
	 */
	public void delete(String id) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		database.delete("t_cart", "id=?", new String[] { id });
		database.close();
	}

	/**
	 *
	 * 
	 * @param id
	 * @param mode
	 */
	public void update(String id, int mode) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		database.update("t_cart", values, "id=?",
				new String[] { id });
		database.close();
	}

	


	/**
	 * 根据pid查找数据
	 * 	values.put("id", id);
		values.put("name", name);
		values.put("price", price);
		values.put("count", count);
		values.put("discount", discount);
	 */
	public ArrayList<Cart> findAll() {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("t_cart", new String[] { "id",
				"name" ,"price","count","discount"}, null, null, null, null, null);

		ArrayList<Cart> list = new ArrayList<Cart>();
		while (cursor.moveToNext()) {
			
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String count = cursor.getString(cursor.getColumnIndex("count"));
			String discount = cursor.getString(cursor.getColumnIndex("discount"));
			
			Cart info = new Cart();
			info.id = id;
			info.name = name;
			info.price = price;
			info.count = count;
			info.discount = discount;
			list.add(info);
		}
		cursor.close();
		database.close();
		return list;
	}
	
	/**
	 * 根据pid查找数据
	 * 	values.put("id", id);
		values.put("name", name);
		values.put("price", price);
		values.put("count", count);
		values.put("discount", discount);
	 */
	public ArrayList<Cart> findById(String id) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("t_cart", new String[] { "id",
				"name" ,"price","count","discount"}, "id=?", new String[]{id}, null, null, null);

		ArrayList<Cart> list = new ArrayList<Cart>();
		while (cursor.moveToNext()) {
			
			String fid = cursor.getString(cursor.getColumnIndex("id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String count = cursor.getString(cursor.getColumnIndex("count"));
			String discount = cursor.getString(cursor.getColumnIndex("discount"));
			
			Cart info = new Cart();
			info.id = fid;
			info.name = name;
			info.price = price;
			info.count = count;
			info.discount = discount;
			list.add(info);
		}
		cursor.close();
		database.close();
		return list;
	}
	
	/**
	 * 根据pid查找数据
	 * 	values.put("id", id);
		values.put("name", name);
		values.put("price", price);
		values.put("count", count);
		values.put("discount", discount);
	 */
	public Cart findByName(String name) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		Cursor cursor = database.query("t_cart", new String[] { "id",
				"name" ,"price","count","discount"}, "name=?", new String[]{name}, null, null, null);

		Cart cart = null;
		while (cursor.moveToNext()) {
			cart = new Cart();
			String fid = cursor.getString(cursor.getColumnIndex("id"));
			String fname = cursor.getString(cursor.getColumnIndex("name"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String count = cursor.getString(cursor.getColumnIndex("count"));
			String discount = cursor.getString(cursor.getColumnIndex("discount"));
			
			Cart info = new Cart();
			info.id = fid;
			info.name = fname;
			info.price = price;
			info.count = count;
			info.discount = discount;
			
		}
		cursor.close();
		database.close();
		return cart;
	}

}
