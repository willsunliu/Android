package com.will.phoneblocker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BlockedListActivity extends Activity {

	private static final String TAG = "-- BlockedListActivity --";
	private static final String SERVICE_INTENT = "com.will.phoneblocker.BLOCK_SERVICE";
//	private static final String TEST = "com.will.phoneblocker.TEST";
	
	private BlockerApplication application;
	private ListView blockedList;
	private Button test;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blocked_list_activity);
		application = (BlockerApplication) getApplication();
		blockedList = (ListView) findViewById(R.id.blocked_list);
		/*
		test = (Button) findViewById(R.id.test);
		test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TEST);
				sendBroadcast(intent);
			}
			
		});
		*/
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		
		switch (item.getItemId()) {
		case R.id.service:
			intent.setAction(SERVICE_INTENT);
			if (application.isServiceRunning()) {
				Toast.makeText(this, "Stop menu", Toast.LENGTH_LONG).show();
				item.setIcon(android.R.drawable.ic_media_play);
				boolean tmp = application.closeAutoStartService();
				Log.i(TAG, "auto_start_service=" + tmp);
				stopService(intent);
			} else {
				Toast.makeText(this, "Start menu", Toast.LENGTH_LONG).show();
				item.setIcon(android.R.drawable.ic_media_pause);
				boolean tmp = application.openAutoStartService();
				Log.i(TAG, "auto_start_service=" + tmp);
				startService(intent);
			}
			break;
		case R.id.blacklistactivity:
			Toast.makeText(this, "BlackList menu", Toast.LENGTH_LONG).show();
			intent.setClass(BlockedListActivity.this, BlackListActivity.class);
			startActivity(intent);
			break;
		case R.id.exit:
			Toast.makeText(this, "Exit menu", Toast.LENGTH_LONG).show();
		}
		return true;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		MenuItem toggleItem = menu.findItem(R.id.service);
		if (application.isServiceRunning()) {
			toggleItem.setTitle(R.string.titleStop);
			toggleItem.setIcon(android.R.drawable.ic_media_pause);
		} else {
			toggleItem.setTitle(R.string.titleStart);
			toggleItem.setIcon(android.R.drawable.ic_media_play);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blocked_list_menu, menu);
		return true;
	}

}
