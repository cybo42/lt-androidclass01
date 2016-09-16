package com.ltree.android.weather;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

/**
 * 
 * @author Course 2771 Development Team
 *
 */
public class WeatherService extends IntentService {
	private static final String TAG="IntentService";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate() called");

	
	}

	public WeatherService()
	{
		super("SyncService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "Processing started");
		
		// Plan was to have them use a Toast here but the Toast gets stuck.
		// The only solution is to Launch the Toast in a thread which is way to complex at this point.
		Log.i(TAG, "Service Started");
		
        // If there is no data associated with the Intent, sets the data to the default URI, which
        // accesses a list of expenses.
        if (intent.getData() == null) {
//            intent.setData(Expense.ExpenseItem.CONTENT_URI);
        } 		
        
        Bundle extras = intent.getExtras();
        if(null != extras){
        	// Note that the flag indicating what action the service should take is retrieved here (nothing to change)
//        	exportToCsvFile = extras.getBoolean(Constants.EXPENSE_EXTRA_EXPORT_TO_CSV);
        	
        }
		
		Log.i(TAG, "Processing finished");	
	}
	
	
}
