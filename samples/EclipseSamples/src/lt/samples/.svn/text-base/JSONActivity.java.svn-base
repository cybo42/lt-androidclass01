package lt.samples;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class JSONActivity extends Activity {

	private static final String sJsonData = "{\"expense\":[{\"description\":\"Severn bridge toll\",\"amount\":\"15.0\",\"expenseDate\":\"1305876297746\"},{\"description\":\"Slap up lunch\",\"amount\":\"2000.0\",\"expenseDate\":\"1396250633843\"}]}";
	private static final String TAG = "JSONActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
			parseJSON(sJsonData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Failed parsing JSON", e);
		}
    }
    
    private void parseJSON(String json) throws JSONException
    {
    	JSONTokener tokenizer = new JSONTokener(json);
    	JSONObject wrapper = (JSONObject)tokenizer.nextValue();
    	JSONArray jExpenses = wrapper.getJSONArray("expense");
		for(int i=0; i < jExpenses.length(); i++){
			Log.i(TAG, "Description = " + jExpenses.getJSONObject(i).getString("description"));
		}

		/*
    	Object obj;
    	while((obj = tokenizer.nextValue()) != null){
    		if(obj instanceof JSONArray){
    			JSONArray jExpenses = (JSONArray) obj;
    			for(int i=0; i < jExpenses.length(); i++){
    				Log.i(TAG, "Description = " + jExpenses.getJSONObject(i).getString("description"));
    			}
    		} else if(obj instanceof JSONObject){
    			JSONObject jExpense = (JSONObject) obj;
    			Log.i(TAG, "Description = " + jExpense.getString("description"));
    		}
   		
    	}
    	*/

    }
}
