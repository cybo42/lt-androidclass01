package com.ltree.expenses;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.ltree.expenses.data.Expense;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 *
 */
public class SyncService extends IntentService {
    private static final String TAG="SyncService";
	private static int NOTIFICATION_ID = 1;

    public SyncService() {
        super("SyncService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate() called");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Processing Started");

		/** Default action is to save to the Web service using JSON */

		Log.i(TAG, "Service Started");


		Log.i(TAG, "Processing finished");
	}


	/**
	 * Send expenses to Web service as JSON data
	 * @param expenses the array of expenses to return
	 */

	private String toJSON(Expense[] expenses) throws JSONException {

			// TODO 21 Use Expense.Helper.expenseArrayToJSON() to convert our expenses to a JSON array
			JSONArray jExpenses = Expense.Helper.expenseArrayToJSON(expenses);
			// TODO 22 Create a new JSONObject to represent the outer wrapper or root of our JSON data
			JSONObject jsonRoot = new JSONObject();
			// TODO 23 put the JSON expenses array into the jsonRoot as a property called expense
			jsonRoot.put("expense", jExpenses);
			// TODO 24 return the JSON data as a String
			return jsonRoot.toString();


	}





	private boolean writeExpensesToCsvFile(String fileName, Expense[] expenses)
	{
		boolean success = false;
		PrintWriter pw = null;

		//  Get the state of the external storage (remove the null!)
		// HINT: See chapter 5
		String state = Environment.getExternalStorageState();

		// Test that the state is Environment.MEDIA_MOUNTED (accessible and writable)
		// Hint: don't forget that state is a String (use equals() to compare)
		if(state.equals(Environment.MEDIA_MOUNTED )){
			try {
				// Get the File object representing the Public Downloads area
				// Hint: use a method on Environment
				File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

				// Ensure the directories in the path are actually created
				baseDir.mkdirs();


				// Create the file name we will use to write the CSV file
				// by appending the fileName to the baseDir
				// Hint: fileName is a parameter to the method you are working in
				File fullFileName = new File(baseDir, fileName);



				Log.d(TAG, "Outputting csv to: " + fullFileName.getAbsolutePath());
				//  Create a new PrintWriter to write the file (use the fullFileName )
				pw = new PrintWriter(fullFileName);


				// Pass the expenses array to Expense.Helper.expenseArrayToCsv then
				// use the PrintWriter to write the data to the CSV file
				// Hint: you may need to convert the returned StringBuilder to a string
				pw.write(Expense.Helper.expenseArrayToCsv(expenses).toString());

				success = true;
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "Failed to save expenses in service. Details:", e);
			} finally {
				if(null != pw){
					// close the PrintWriter
                    pw.close();
				}
			}
		}
		return success;
	}

	/**
	 * Helper method to set either a default file name or use one passed in with the intent
	 * @param intent
	 * @return the file name to write to
	 */
	private static String resolveFileNameForExpenses(Intent intent) {
		String fileName = Constants.DEFAULT_CSV_FILENAME; // set a default file name
        Bundle extras = intent.getExtras();
        if(null != extras ){
	        String tmpFName = extras.getString(Constants.EXPENSE_EXTRA_FILENAME);
	        if(null != tmpFName){
	        	fileName = tmpFName;
	        }
        }
		return fileName;
	}



	/** Dummy class to allow a notification to be created which does not actually do anything! */
	class DummyActivity extends Activity{
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			finish();
		}
    }
}
