package com.will.phoneblocker;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlockerApplication extends Application {

	private static final String DATABASE_NAME = "BlackList.db3";
	public static final String AUTO_SERVICE = "auto_service";
	public static final String AUTO_SERVICE_ITEM = "auto_start_service";
	public static final String TABLE_NAME = "blacklist";
	
	private boolean serviceRunning;
	private MySQLiteOpenHelper dbHelper;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dbHelper = new MySQLiteOpenHelper(this, DATABASE_NAME, 1);
		preferences = this.getSharedPreferences(AUTO_SERVICE, MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	public boolean openAutoStartService() {
		editor.putBoolean(AUTO_SERVICE_ITEM, true);
		editor.commit();
		return preferences.getBoolean(AUTO_SERVICE_ITEM, false);
	}

	public boolean closeAutoStartService() {
		editor.putBoolean(AUTO_SERVICE_ITEM, false);
		editor.commit();
		return preferences.getBoolean(AUTO_SERVICE_ITEM, true);
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (dbHelper != null) {
			dbHelper.close();
		}
		super.onTerminate();
	}

	public boolean isBlack(String phone) {
		ArrayList<HashMap<String, String>> blackNumberList = getBlackList();
		for (int i = 0; i < blackNumberList.size(); i++) {
			HashMap<String, String> map = blackNumberList.get(i);
			for (Object key : map.keySet()) {
				if (phone.equals(map.get(key))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList<HashMap<String, String>> getBlackList() {
		ArrayList<HashMap<String, String>> blackNumberList = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
			blackNumberList.add(map);
		}
		
		return blackNumberList;
	}
	
	public void insertData(String name, String phone) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.execSQL("insert into " + TABLE_NAME + " values(null, ?, ?)", new String[] {name, phone});
	}
	
	public boolean isServiceRunning() {
		return this.serviceRunning;
	}
	
	public void setServiceRunning(boolean isServiceRunning) {
		this.serviceRunning = isServiceRunning;
	}
}
