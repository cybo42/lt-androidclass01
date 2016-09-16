package lt.samples;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class StorageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storePreference("serverUrl", "http://localhost/server");
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		String url = getPreference("serverUrl");
		Toast toast = Toast.makeText(this, url, Toast.LENGTH_LONG);
		toast.show();
		
	}

	private static final String PREFS_NAME = "appPrefs";
	
	private void storePreference(String name, String value){
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();        	
	}
	
	private String getPreference(String name){
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
	    return settings.getString(name, "default");
	}
	

	
	BroadcastReceiver mExternalStorageReceiver;
	boolean mExtStoragePresent = false;
	boolean mExtStorageRdOnly = true;

	void checkExternalStorage() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	    		// External storage mounted and writable
	        mExtStoragePresent = true;
	        mExtStorageRdOnly = false;
	    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	    		// External storage mounted but read only
	        mExtStoragePresent = true;
	        mExtStorageRdOnly = true;
	    } else {
	    		// No storage present
	        mExtStoragePresent = false;
	        mExtStorageRdOnly = true;
	    }
	}	
	
	@SuppressWarnings("unused")
	private void registerBroadcastReceiver()
	{
		// Create a receiver class
		mExternalStorageReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	// If notified of state change, call our checkExternalStorage() method
	            checkExternalStorage();
	        }
	    };
	    // Define an IntentFilter
	    IntentFilter filter = new IntentFilter();
	    // Specify actions to be notified of
	    filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
	    filter.addAction(Intent.ACTION_MEDIA_REMOVED);
	    // Register the receiver
	    registerReceiver(mExternalStorageReceiver, filter);
	}
}
