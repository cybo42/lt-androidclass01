package com.ltree.android.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WeatherActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }
    
    private void startWeatherService()
    {
    	Intent startSyncService = new Intent(this, 
    								WeatherService.class);
         startService(startSyncService);	     
    }    
    
}