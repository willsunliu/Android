package com.will.phoneblocker;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BlockService extends Service {

	private static final String TAG = "-- BlockService --";
	
	private BlockerApplication application;
	private TelephonyManager tManager;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreate");
		super.onCreate();
		application = (BlockerApplication) getApplication();
		application.setServiceRunning(true);
		
		tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
		PhoneStateListener listener = new PhoneStateListener() {

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// TODO Auto-generated method stub
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					Log.i(TAG, "phone number --> " + incomingNumber);
					if (application.isBlack(incomingNumber)) {
						Log.i(TAG, incomingNumber + "is black number");
						try {
							Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
							IBinder binder = (IBinder) method.invoke(null, new Object[] { TELEPHONY_SERVICE });
							ITelephony telephony = ITelephony.Stub.asInterface(binder);
							telephony.endCall();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}
			
		};
		tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		application.setServiceRunning(false);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

}
