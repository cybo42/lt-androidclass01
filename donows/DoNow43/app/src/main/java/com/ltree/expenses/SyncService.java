package com.ltree.expenses;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SyncService extends IntentService {
	private static final String TAG="SyncService";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate() called");

	
	}

	public SyncService()
	{
		super("SyncService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "Processing started");
		
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i(TAG, "Processing finished");	
	}

}
